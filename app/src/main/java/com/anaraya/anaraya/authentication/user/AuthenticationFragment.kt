package com.anaraya.anaraya.authentication.user

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.AuthAdapter
import com.anaraya.anaraya.authentication.user.resetpassword.ResetPasswordFragment
import com.anaraya.anaraya.authentication.user.signin.SignInFragment
import com.anaraya.anaraya.authentication.user.signup.SignUpFragment
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
        lifecycleScope.launch {
            viewModel.stateViewPager.collectLatest {
                binding.tabAuth.removeAllTabs()
                list = if (it == 0) {
                    binding.tabAuth.addTab(binding.tabAuth.newTab().setText(getString(R.string.sign_in)))
                    binding.tabAuth.addTab(binding.tabAuth.newTab().setText(getString(R.string.sign_up)))
                    viewModel.resetSignInResponse()
                    arrayListOf(
                        SignInFragment(),
                        SignUpFragment()
                    )
                } else {
                    binding.tabAuth.addTab(binding.tabAuth.newTab().setText(getString(R.string.reset_password)))
                    viewModel.resetResetPassResponse()
                    arrayListOf(
                        ResetPasswordFragment(),
                    )
                }
                adapter = AuthAdapter(list, childFragmentManager, lifecycle)
                binding.viewPagerAuth.adapter = adapter
            }
        }

        lifecycleScope.launch {
            viewModel.signUpResponse.collectLatest {
                if (it.navigateToAddAddress) {
                    findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToAddAddressSignUpFragment())
                    viewModel.navigateToAddAddressDone()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.navigateToFamilyState.collectLatest {
                if (it) {
                    findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToAuthenticationFamilyFragment())
                    viewModel.navigateToFamilyDone()
                }
            }
        }



        onPageSelected = getOnPageChangeCallBack()
        binding.tabAuth.addOnTabSelectedListener(getTabOnSelectedListener())
        binding.viewPagerAuth.registerOnPageChangeCallback(onPageSelected)


        binding.btnArrowBackSignIn.setOnClickListener {
            if (viewModel.stateViewPager.value == 0) {
                if (viewModel.statePage.value == 0)
                    setStateSignInMinus()
                else
                    setStateSignUpMinus()
            } else {
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
            else -> findNavController().popBackStack()
        }
    }

    private fun setStateSignUpMinus() {
        var num = viewModel.stateSignIn.value
        when (num) {
            2 -> viewModel.setStateSignUpUpdate(--num)
            else -> {
                viewModel.setStatePage(0)
                binding.tabAuth.selectTab(binding.tabAuth.getTabAt(0))
            }
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