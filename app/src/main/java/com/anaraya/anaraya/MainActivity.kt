package com.anaraya.anaraya

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anaraya.anaraya.databinding.ActivityMainBinding
import com.anaraya.anaraya.util.setLocal
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var backPressedOnce = false
    private val backPressHandler = Handler(Looper.getMainLooper())
    private val backPressRunnable = Runnable { backPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window,false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_splash_Main) as NavHostFragment
        binding.toolBarHome.setupWithNavController(navHostFragment.navController)

        setLocal(this, sharedPreferences.getString("lang", "en")!!)
        setContentView(binding.root)

        if (intent.extras != null) {
            try {
                sharedPreferences.edit()
                    .putString("notificationType", intent.getStringExtra("notificationType")).apply()
                sharedPreferences.edit().putString("itemId", intent.getStringExtra("itemId")).apply()
            }catch (_:Exception){

            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(navHostFragment.childFragmentManager.backStackEntryCount > 0){
                    supportFragmentManager.popBackStack()
                    return
                }
                if (backPressedOnce) {
                    finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(applicationContext,
                        getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show()
                    backPressHandler.postDelayed(backPressRunnable, 2000)
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        backPressHandler.removeCallbacks(backPressRunnable)
    }
}