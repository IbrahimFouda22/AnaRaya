package com.anaraya.anaraya.landing.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.activity.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class Splash2Fragment : Fragment() {
    private val navArgs by navArgs<Splash2FragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Handler().postDelayed({
            if (navArgs.navigate == "onBoard")
                findNavController().navigate(Splash2FragmentDirections.actionSplash2FragmentToViewPagerFragment())
            else{
                if(navArgs.navigate == "auth")
                    findNavController().navigate(Splash2FragmentDirections.actionSplash2FragmentToAuthenticationFragment())
                else {
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()
                }
            }
        }, 400)
        return inflater.inflate(R.layout.fragment_splash2, container, false)
    }


}