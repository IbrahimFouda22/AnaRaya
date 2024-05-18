package com.anaraya.anaraya.authentication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.anaraya.anaraya.authentication.resetpassword.ResetPasswordFragment
import com.anaraya.anaraya.authentication.signin.SignInFragment
import com.anaraya.anaraya.authentication.signup.SignUpFragment
import com.anaraya.anaraya.databinding.FragmentAuthenticationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {
    private lateinit var binding: FragmentAuthenticationBinding
    private lateinit var onPageSelected: ViewPager2.OnPageChangeCallback
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModels<AuthViewModel>({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        var list: ArrayList<Fragment>
        var adapter: AuthAdapter
//        Log.d("state", "${sharedPreferences.getBoolean(context.getString(R.string.signupstate),false)}")
//        if(sharedPreferences.getBoolean(context.getString(R.string.signupstate),false)) {
//            setStateSignUpUpdate(2)
//            sharedPreferences.edit().putBoolean(context.getString(R.string.signupstate),false).apply()
//        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateViewPager.collectLatest{
                binding.tabAuth.removeAllTabs()
                list = if(it == 0){
                    binding.tabAuth.addTab(binding.tabAuth.newTab().setText("Sign in"))
                    binding.tabAuth.addTab(binding.tabAuth.newTab().setText("Sign up"))
                    viewModel.resetSignInResponse()
                    arrayListOf(
                        SignInFragment(),
                        SignUpFragment()
                    )
                } else{
                    binding.tabAuth.addTab(binding.tabAuth.newTab().setText("Reset Password"))
                    viewModel.resetResetPassResponse()
                    arrayListOf(
                        ResetPasswordFragment(),
                    )
                }
                adapter = AuthAdapter(list, childFragmentManager, lifecycle)
                binding.viewPagerAuth.adapter = adapter
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpResponse.collectLatest {
                if(it.navigateToAddAddress){
                    findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToAddAddressSignUpFragment())
                    viewModel.navigateToAddAddressDone()
                }
            }
        }



        onPageSelected = getOnPageChangeCallBack()
        binding.tabAuth.addOnTabSelectedListener(getTabOnSelectedListener())
        binding.viewPagerAuth.registerOnPageChangeCallback(onPageSelected)


        binding.btnArrowBackSignIn.setOnClickListener {
            if(viewModel.stateViewPager.value == 0){
                if (viewModel.statePage.value == 0)
                    setStateSignInMinus()
                else
                    setStateSignUpMinus()
            }
            else{
                setStateResetPassMinus()
            }
        }

        return binding.root
    }

    private fun getTabOnSelectedListener(): TabLayout.OnTabSelectedListener {
        return object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                if (tab != null) {
                    binding.viewPagerAuth.currentItem = tab.position
                    viewModel.setStatePage(tab.position)
                }
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabReselected(tab: Tab?) {
            }

        }
    }

    private fun getOnPageChangeCallBack(): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabAuth.selectTab(binding.tabAuth.getTabAt(position))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPagerAuth.unregisterOnPageChangeCallback(onPageSelected)
    }

    private fun setStateSignInMinus() {
        var num = viewModel.stateSignIn.value
        when (num) {
            3 -> viewModel.setStateSignInUpdate(--num)
            2 -> viewModel.setStateSignInUpdate(--num)
            else -> viewModel.setStateSignInUpdate(num)
        }
    }

    private fun setStateSignUpMinus() {
        var num = viewModel.stateSignIn.value
        when (num) {
            2 -> viewModel.setStateSignUpUpdate(--num)
            else -> viewModel.setStateSignUpUpdate(num)
        }
    }

    private fun setStateResetPassMinus() {
        var num = viewModel.stateResetPass.value
        when (num) {
            3 -> viewModel.setStateResetPassUpdate(1)
            2 -> viewModel.setStateResetPassUpdate(--num)
            else -> viewModel.setStateViewPagerUpdate(0)
        }
    }

}