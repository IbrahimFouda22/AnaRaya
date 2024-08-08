@file:Suppress("DEPRECATION")

package com.anaraya.anaraya.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import java.util.Locale

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun setLocal(activity: Activity, lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val resources = activity.resources
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun showToolBar(activity: Activity, boolean: Boolean) {
    val toolBar = activity.findViewById<MaterialToolbar>(R.id.toolBarActivity)
    if (boolean)
        toolBar.visible()
    else
        toolBar.gone()

}

fun showMainToolBar(activity: Activity, boolean: Boolean) {
    val toolBar = activity.findViewById<MaterialToolbar>(R.id.toolBarHome)
    if (boolean)
        toolBar.visible()
    else
        toolBar.gone()

}

fun showBottomNavBar(activity: Activity, boolean: Boolean) {
    val bottomNav = activity.findViewById<ChipNavigationBar>(R.id.bottomNavHome)
    if (boolean)
        bottomNav.visible()
    else
        bottomNav.gone()

}

fun showCardHome(activity: Activity, boolean: Boolean) {
    val cardHome = activity.findViewById<CardView>(R.id.cardSearch_homeActivity)
    if (boolean)
        cardHome.visible()
    else
        cardHome.gone()
}

fun showTextSchedule(activity: Activity, boolean: Boolean) {
    val textSchedule = activity.findViewById<TextView>(R.id.txtDelivery_homeActivity)
    if (boolean)
        textSchedule.visible()
    else
        textSchedule.gone()
}

fun showButtonCart(activity: Activity, boolean: Boolean) {
    val btnCart = activity.findViewById<ImageButton>(R.id.btnCart)
    if (boolean)
        btnCart.visible()
    else
        btnCart.gone()
}

fun showTextBadge(
    activity: Activity,
    boolean: Boolean,
    sharedPreferences: SharedPreferences,
    context: Context
) {
    val num = sharedPreferences.getInt(context.getString(R.string.productsinbasket), 0)
    val textBadge = activity.findViewById<TextView>(R.id.txtBadge)
    if (num > 0 && boolean) {
        textBadge.visible()
    } else {
        textBadge.gone()
    }

}

fun showButtonFilter(activity: Activity, boolean: Boolean) {
    val btnFilter = activity.findViewById<ImageButton>(R.id.btnFilter)
    if (boolean)
        btnFilter.visible()
    else
        btnFilter.gone()
}

fun showButtonFav(activity: Activity, boolean: Boolean) {
    val btnFav = activity.findViewById<ImageButton>(R.id.btnFavHome)
    if (boolean)
        btnFav.visible()
    else
        btnFav.invisible()
}

fun removeUser(sharedPreferences: SharedPreferences, context: Context) {
    sharedPreferences.edit().putString("token", null).apply()
    sharedPreferences.edit().putBoolean("auth", true).apply()
    sharedPreferences.edit().putString("rayaId", null).apply()
    sharedPreferences.edit().putString("nationalId", null).apply()
    sharedPreferences.edit().putString("password", null).apply()
    sharedPreferences.edit().putInt(context.getString(R.string.productsinbasket), 0).apply()
    sharedPreferences.edit().putBoolean(context.getString(R.string.signupstate), false).apply()
    sharedPreferences.edit().putBoolean("isFamily", false).apply()
}

fun minusNumBasket(
    sharedPreferences: SharedPreferences,
    sharedViewModel: HomeActivityViewModel,
    context: Context
) {
    var num = sharedPreferences.getInt(context.getString(R.string.productsinbasket), 0)
    num--
    sharedPreferences.edit().putInt(context.getString(R.string.productsinbasket), num).apply()
    sharedViewModel.setProductInBasket(num)
}

fun plusNumBasket(
    sharedPreferences: SharedPreferences,
    sharedViewModel: HomeActivityViewModel,
    context: Context
) {
    var num = sharedPreferences.getInt(context.getString(R.string.productsinbasket), 0)
    num++
    sharedPreferences.edit().putInt(context.getString(R.string.productsinbasket), num).apply()
    sharedViewModel.setProductInBasket(num)
}

fun plusQtyBasket(
    sharedPreferences: SharedPreferences,
    sharedViewModel: HomeActivityViewModel,
    context: Context,
    qty: Int
) {
    var num = sharedPreferences.getInt(context.getString(R.string.productsinbasket), 0)
    num += qty
    sharedPreferences.edit().putInt(context.getString(R.string.productsinbasket), num).apply()
    sharedViewModel.setProductInBasket(num)
}

fun resetQtyBasket(
    sharedPreferences: SharedPreferences,
    sharedViewModel: HomeActivityViewModel,
    context: Context,
) {
    sharedPreferences.edit().putInt(context.getString(R.string.productsinbasket), 0).apply()
    sharedViewModel.setProductInBasket(0)
}

fun TextView.copyText(activity: Activity,context: Context){
    this.setOnLongClickListener {
        val textToCopy = this.text.toString()
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(context.getString(R.string.copied_text), textToCopy)
        clipboard.setPrimaryClip(clip)
        // Optionally show a toast or other feedback to the user
        Toast.makeText(context,
            context.getString(R.string.text_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        true
    }
}



