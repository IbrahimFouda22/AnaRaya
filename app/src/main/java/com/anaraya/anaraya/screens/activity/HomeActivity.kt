package com.anaraya.anaraya.screens.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.ActivityHomeBinding
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

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window,false)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
//        setContentView(binding.root)
        handleIntent()
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHome) as NavHostFragment
//        binding.bottomNavHome.setupWithNavController(navHostFragment.navController)

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
            }
        }

        binding.toolBarActivity.setupWithNavController(navHostFragment.navController)
        binding.bottomNavHome.setItemSelected(R.id.homeFragment)
        binding.bottomNavHome.setOnItemSelectedListener {
            navigatePage(it)
        }

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
                if(id != null){
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
                    }
                }
                else{
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
        }
        else{
            val notificationRedirectTo = sharedPreferences.getString("notificationType", null)
            val id = sharedPreferences.getString("itemId", null)
            notificationRedirectTo?.let {
                if(id != null){
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
                    }
                }
                else{
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

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        super.onBackPressed()
//        when(navHostFragment.navController.currentBackStackEntry){
//            navHostFragment.navController.getBackStackEntry(R.id.homeFragment) -> binding.bottomNavHome.setItemSelected(R.id.homeFragment)
//            navHostFragment.navController.getBackStackEntry(R.id.servicesFragment) -> binding.bottomNavHome.setItemSelected(R.id.servicesFragment)
//            navHostFragment.navController.getBackStackEntry(R.id.moreFragment) -> binding.bottomNavHome.setItemSelected(R.id.moreFragment)
//        }
//    }
}