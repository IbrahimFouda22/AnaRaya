package com.anaraya.anaraya.screens.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anaraya.anaraya.MainActivity
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.ActivityHomeBinding
import com.anaraya.anaraya.databinding.LayoutDialogBanBinding
import com.anaraya.anaraya.util.TokenRefreshState
import com.anaraya.anaraya.util.removeUser
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel: HomeActivityViewModel by viewModels()
    private var backPressedOnce = false
    private val backPressHandler = Handler(Looper.getMainLooper())
    private val backPressRunnable = Runnable { backPressedOnce = false }

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window,false)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val isFamily = sharedPreferences.getBoolean("isFamily", false)

//        setContentView(binding.root)
        handleIntent()
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHome) as NavHostFragment
//        binding.bottomNavHome.setupWithNavController(navHostFragment.navController)

//        navHostFragment.navController.navigate(R.id.searchFragment)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    return
                }
                if (backPressedOnce) {
                    finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT
                    ).show()
                    backPressHandler.postDelayed(backPressRunnable, 2000)
                }
            }
        })
        lifecycleScope.launch {
            viewModel.homeState.collectLatest {
                if (it.error != null) {
                    if (it.error == applicationContext.getString(R.string.no_internet)) showToolBar(
                        this@HomeActivity,
                        false
                    )
                }
                if (it.isFCMSent) {
                    sharedPreferences.edit().putBoolean("fcm_token_sent", true).apply()
                    viewModel.resetFCM()
                }
                if (it.navigateToBrand) {
                    val bundle = Bundle().apply {
                        putInt("selectionType", 4)
                        putString("id",viewModel.homeState.value.navigationId)
                    }
                    navHostFragment.navController.navigate(R.id.searchFragment,bundle)
                    viewModel.navigateToBrandDone()
                }
                if (it.navigateToMainCat) {
                    val bundle = Bundle().apply {
                        putInt("selectionType", 2)
                        putString("id",viewModel.homeState.value.navigationId)
                    }
                    navHostFragment.navController.navigate(R.id.searchFragment,bundle)
                    viewModel.navigateToMainCatDone()
                }
                if (it.navigateToCat) {
                    val bundle = Bundle().apply {
                        putInt("selectionType", 3)
                        putString("id",viewModel.homeState.value.navigationId)
                    }
                    navHostFragment.navController.navigate(R.id.searchFragment,bundle)
                    viewModel.navigateToCatDone()
                }
                if (it.navigateToProduct) {
                    val bundle = Bundle().apply {
                        putInt("productId", viewModel.homeState.value.navigationId.toInt())
                        putBoolean("isPoints",false)
                    }
                    navHostFragment.navController.navigate(R.id.searchFragment,bundle)
                    viewModel.navigateToProductDone()
                }
                if (it.navigateToSurvey) {
                    navHostFragment.navController.navigate(R.id.surveysFragment)
                    viewModel.navigateToSurveyDone()
                }
                if (it.navigateToMarketPlaceProduct) {
                    navHostFragment.navController.navigate(R.id.storeFragment)
                    viewModel.navigateToMarketPlaceProductDone()
                }
                if (it.navigateToMarketPlaceOwnerProduct) {
                    val bundle = Bundle().apply {
                        putInt("productId", viewModel.homeState.value.navigationId.toInt())
                    }
                    navHostFragment.navController.navigate(R.id.itemDetailsFragment,bundle)
                    viewModel.navigateToMarketPlaceOwnerProductDone()
                }
                if (it.navigateToMarketPlaceService) {
                    navHostFragment.navController.navigate(R.id.storeServiceFragment)
                    viewModel.navigateToMarketPlaceServiceDone()
                }
                if (it.navigateToMarketPlaceOwnerService) {
                    val bundle = Bundle().apply {
                        putInt("serviceId", viewModel.homeState.value.navigationId.toInt())
                    }
                    navHostFragment.navController.navigate(R.id.serviceDetailsOwnerFragment,bundle)
                    viewModel.navigateToMarketPlaceOwnerServiceDone()
                }
            }
        }

        lifecycleScope.launch {
            TokenRefreshState.isDeleted.collectLatest {
                if (it) {
                    val view = LayoutDialogBanBinding.inflate(layoutInflater)
                    val dialog = Dialog(this@HomeActivity)
                    dialog.setCancelable(false)
                    dialog.setContentView(view.root)
                    dialog.window?.setBackgroundDrawableResource(R.drawable.ban_dialog_shape)
                    dialog.show()
                    view.txtOkBan.setOnClickListener {
                        dialog.dismiss()
                        logOut()
                    }
                }
            }
        }


        binding.toolBarActivity.setupWithNavController(navHostFragment.navController)
        binding.bottomNavHome.setItemSelected(R.id.homeFragment)
        binding.bottomNavHome.setOnItemSelectedListener {
            if(isFamily)
                navigatePageFamily(it)
            else
                navigatePage(it)
        }

        if(isFamily)
            binding.bottomNavHome.setMenuResource(R.menu.menu_chip_family)
        if (sharedPreferences.getBoolean(
                getString(R.string.show_pop_schedule),
                false
            )
        ) showSnackBar()

    }

    private fun navigatePage(id: Int) {
//        R.id.marketFragment -> navHostFragment.navController.navigate(R.id.action_global_marketFragment)
        when (id) {
            R.id.homeFragment -> navHostFragment.navController.navigate(R.id.action_global_homeFragment)
            R.id.servicesFragment -> navHostFragment.navController.navigate(R.id.action_global_servicesFragment)
            R.id.moreFragment -> navHostFragment.navController.navigate(R.id.action_global_moreFragment)
        }
    }
    private fun navigatePageFamily(id: Int) {
//        R.id.marketFragment -> navHostFragment.navController.navigate(R.id.action_global_marketFragment)
        when (id) {
            R.id.homeFragment -> navHostFragment.navController.navigate(R.id.action_global_homeFragment)
            R.id.moreFragment -> navHostFragment.navController.navigate(R.id.action_global_moreFragment)
        }
    }

    @SuppressLint("CommitTransaction")
    private fun showSnackBar() {
        //val view = findViewById<ConstraintLayout>(R.id.consHome)
        Snackbar.make(binding.consHome, getString(R.string.see_schedule), Snackbar.LENGTH_LONG)
            .setAnchorView(binding.bottomNavHome).setAction(getString(R.string.ok)) {
                navHostFragment.navController.navigate(R.id.scheduleFragment)

            }.setActionTextColor(getColor(R.color.white))
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show()
    }

    override fun onStart() {
        super.onStart()
        val token = sharedPreferences.getString("fcm_token", "")
        if (token != null) {
            viewModel.sendFCMToken(
                token = token, enabled = true, isUpdate = false
            )
        }
    }

    override fun onStop() {
        super.onStop()
        sharedPreferences.edit().putBoolean(getString(R.string.show_pop_schedule), true).apply()
    }

    //    @SuppressLint("MissingSuperCall")
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        handleIntent()
//    }
    private fun handleIntent() {
        if (intent.extras != null) {
//            val notificationRedirectTo = intent.getStringExtra("notificationRedirect")
//            val id = intent.getStringExtra("itemId")
            val notificationRedirectTo = sharedPreferences.getString("notificationType", null)
            val id = sharedPreferences.getString("itemId", null)
            notificationRedirectTo?.let {
                if (id != null) {
                    when (notificationRedirectTo) {
                        "Brand" -> {
                            viewModel.navigateToBrand(id)
                        }

                        "MainCategory" -> {
                            viewModel.navigateToMainCat(id)
                        }

                        "Category" -> {
                            viewModel.navigateToCat(id)
                        }

                        "Product" -> {
                            viewModel.navigateToProduct(id)
                        }

                        "MarketPlaceProduct" -> {
                            viewModel.navigateToMarketPlaceOwnerProduct(id)
                        }

                        "MarketPlaceService" -> {
                            viewModel.navigateToMarketPlaceOwnerService(id)
                        }
                    }
                } else {
                    when (notificationRedirectTo) {
                        "Survey" -> {
                            viewModel.navigateToSurvey()
                        }

                        "MarketPlaceProduct" -> {
                            viewModel.navigateToMarketPlaceProduct()
                        }

                        "MarketPlaceService" -> {
                            viewModel.navigateToMarketPlaceService()
                        }
                    }
                }
            }
        } else {
            val notificationRedirectTo = sharedPreferences.getString("notificationType", null)
            val id = sharedPreferences.getString("itemId", null)
            notificationRedirectTo?.let {
                if (id != null) {
                    when (notificationRedirectTo) {
                        "Brand" -> {
                            viewModel.navigateToBrand(id)
                        }

                        "MainCategory" -> {
                            viewModel.navigateToMainCat(id)
                        }

                        "Category" -> {
                            viewModel.navigateToCat(id)
                        }

                        "Product" -> {
                            viewModel.navigateToProduct(id)
                        }

                        "MarketPlaceProduct" -> {
                            viewModel.navigateToMarketPlaceOwnerProduct(id)
                        }

                        "MarketPlaceService" -> {
                            viewModel.navigateToMarketPlaceOwnerService(id)
                        }
                    }
                } else {
                    when (notificationRedirectTo) {
                        "Survey" -> {
                            viewModel.navigateToSurvey()
                        }

                        "MarketPlaceProduct" -> {
                            viewModel.navigateToMarketPlaceProduct()
                        }

                        "MarketPlaceService" -> {
                            viewModel.navigateToMarketPlaceService()
                        }
                    }
                }
            }
            sharedPreferences.edit().putString("notificationType", null).apply()
            sharedPreferences.edit().putString("itemId", null).apply()
        }
        sharedPreferences.edit().putString("notificationType", null).apply()
        sharedPreferences.edit().putString("itemId", null).apply()
    }


    override fun onDestroy() {
        super.onDestroy()
        backPressHandler.removeCallbacks(backPressRunnable)
    }

    private fun logOut() {
        removeUser(sharedPreferences, applicationContext)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}