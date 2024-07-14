package com.anaraya.anaraya.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.activity.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(
            remoteMessage.notification!!.title!!,
            remoteMessage.notification!!.body!!,
            remoteMessage.data["notificationType"].toString(),
            remoteMessage.data["itemId"],
        )
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        token?.let {
            sharedPreferences.edit().putString("fcm_token", it).apply()
        }
    }

    private fun sendNotification(
        messageTitle: String,
        messageBody: String,
        notificationRedirectTo: String,
        itemId: String?,
    ) {

        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("notificationRedirect", notificationRedirectTo)
            putExtra("itemId", itemId)
            sharedPreferences.edit()
                .putString("notificationType", notificationRedirectTo).apply()
            sharedPreferences.edit().putString("itemId", itemId).apply()
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(false)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Ensure high priority for heads-up notification
            .setDefaults(NotificationCompat.DEFAULT_ALL)  // Ensure default vibration and sound
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}