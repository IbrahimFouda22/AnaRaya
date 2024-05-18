package com.anaraya.anaraya.landing.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.anaraya.anaraya.animation.DepthPageTransformer
import com.anaraya.anaraya.databinding.FragmentViewPagerBinding
import com.anaraya.anaraya.landing.onboarding.screen.Screen1Fragment
import com.anaraya.anaraya.landing.onboarding.screen.Screen2Fragment
import com.anaraya.anaraya.util.invisible
import com.anaraya.anaraya.util.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentViewPagerBinding
    private val page = getOnPageChangedObject()
    private val viewModel by viewModels<ViewPagerViewModel>({this})
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(layoutInflater)
        binding.viewPagerOnboard.registerOnPageChangeCallback(page)
        val fragment = arrayListOf(
            Screen1Fragment(),
            Screen2Fragment()
        )
        val adapter = ViewPagerAdapter(fragment,childFragmentManager,lifecycle)

        binding.viewPagerOnboard.adapter = adapter
        binding.viewPagerOnboard.setPageTransformer(DepthPageTransformer())
        binding.dotsIndicator.attachTo(binding.viewPagerOnboard)

        binding.txtSkipOnBoard.setOnClickListener {
            viewModel.navigateToHomeDesign()
        }

        binding.btnGetStarted.setOnClickListener {
            viewModel.navigateToLogin()
        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner){
            if(it){
                sharedPreferences.edit().putBoolean("onBoard",false).apply()
                findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToLanguageFragment())
                viewModel.navigateToLoginDone()
            }
        }

        viewModel.navigateToHomeDesign.observe(viewLifecycleOwner){
            if(it){
                sharedPreferences.edit().putBoolean("onBoard",false).apply()
                findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToHomeDesignFragment())
                viewModel.navigateToHomeDesignDone()
            }
        }

        return binding.root
    }

    private fun getOnPageChangedObject():OnPageChangeCallback{
        return object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        binding.txtSkipOnBoard.visible()
                        binding.btnGetStarted.invisible()
                    }
                    1 -> {
                        binding.txtSkipOnBoard.invisible()
                        binding.btnGetStarted.visible()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPagerOnboard.unregisterOnPageChangeCallback(page)
    }
}