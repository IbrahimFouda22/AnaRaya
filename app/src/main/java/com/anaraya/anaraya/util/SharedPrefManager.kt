package com.anaraya.anaraya.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefManager @Inject constructor(@ApplicationContext private val context: Context) {
    fun getSharedPref() : SharedPreferences {
        // Initialize/open an instance of EncryptedSharedPreferences on below line.
        return EncryptedSharedPreferences.create(
            // passing a file name to share a preferences
            context,
            "secret_shared_prefs",
            getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    }
}