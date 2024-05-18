package com.anaraya.anaraya.landing.splash

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class Splash1Fragment @Inject constructor() : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var token: String? = null
    private var rayaId: String? = null
    private var nationalId: String? = null
    private var password: String? = null
    private var auth: Boolean = false
    private var home: Boolean = true
    private var onBoard: Boolean = false
    private val viewModel: Splash1ViewModel by viewModels({this})
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Handler().postDelayed({
//            lifecycleScope.launch {
//                viewModel.checkState.collectLatest{
//                    if (it.error.isNullOrEmpty())
//                        home = true
//                    if(it.error == "unAuth")
//                        home = false
//                }
//            }
            lifecycleScope.launch {
                token = sharedPreferences.getString("token",null)
                auth = sharedPreferences.getBoolean("auth",true)
                onBoard = sharedPreferences.getBoolean("onBoard",true)
            }
            if (onBoard)
            findNavController().navigate(Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment("onBoard"))
            else{
                if(auth) {
                    findNavController().navigate(Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment("auth"))
                }
                else {
                    if(token!=null){
                        //Log.d("token", token!!)
                        findNavController().navigate(
                            Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment(
                                "home"
                            )
                        )
                    }
                    /*if(token!=null){
                        rayaId = sharedPreferences.getString("rayaId",null)
                        nationalId = sharedPreferences.getString("nationalId",null)
                        password = sharedPreferences.getString("password",null)
                        if(rayaId == null || nationalId == null || password == null){
                            findNavController().navigate(
                                Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment(
                                    "auth"
                                )
                            )
                        }else {
                            viewModel.checkAuth(rayaId!!, nationalId!!, password!!)
                            if (home)
                                findNavController().navigate(
                                    Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment(
                                        "home"
                                    )
                                )
                            else
                                findNavController().navigate(
                                    Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment(
                                        "auth"
                                    )
                                )
                        }
                    }*/
                }
            }
        }, 2200)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}