package com.anaraya.anaraya.landing.splash

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Splash1Fragment @Inject constructor() : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var token: String? = null
    private var auth: Boolean = false
    private var onBoard: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_splash, container, false)
        val motionLayout = v.findViewById<MotionLayout>(R.id.constraintSplash)
        motionLayout.addTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                token = sharedPreferences.getString("token",null)
                auth = sharedPreferences.getBoolean("auth",true)
                onBoard = sharedPreferences.getBoolean("onBoard",true)
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
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
                        else{
                            findNavController().navigate(
                                Splash1FragmentDirections.actionSplashFragmentToSplash2Fragment(
                                    "auth"
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
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
        return v
    }

}