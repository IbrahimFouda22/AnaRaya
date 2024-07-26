package com.anaraya.anaraya.authentication.family

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
import com.anaraya.anaraya.authentication.family.reset_pass_family.ResetPasswordFamilyFragment
import com.anaraya.anaraya.authentication.family.signin_family.SignInFamilyFragment
import com.anaraya.anaraya.authentication.family.signup_family.SignUpFamilyFragment
import com.anaraya.anaraya.authentication.user.AuthenticationFragmentDirections
import com.anaraya.anaraya.authentication.user.resetpassword.ResetPasswordFragment
import com.anaraya.anaraya.databinding.FragmentAuthenticationFamilyBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationFamilyFragment : Fragment() {
    private lateinit var binding: FragmentAuthenticationFamilyBinding
    private lateinit var onPageSelected: ViewPager2.OnPageChangeCallback

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModels<AuthFamilyViewModel>({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationFamilyBinding.inflate(layoutInflater)
        var list: ArrayList<Fragment>
        var adapter: AuthAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateViewPager.collectLatest {
                binding.tabAuthFamily.removeAllTabs()
                list = if (it == 0) {
                    binding.tabAuthFamily.addTab(binding.tabAuthFamily.newTab().setText(getString(R.string.sign_in)))
                    binding.tabAuthFamily.addTab(binding.tabAuthFamily.newTab().setText(getString(R.string.sign_up)))
                    viewModel.resetSignInResponse()
                    arrayListOf(
                        SignInFamilyFragment(),
                        SignUpFamilyFragment()
                    )
                } else {
                    binding.tabAuthFamily.addTab(binding.tabAuthFamily.newTab().setText(getString(R.string.reset_password)))
                    viewModel.resetResetPassResponse()
                    arrayListOf(
                        ResetPasswordFamilyFragment(),
                    )
                }
                adapter = AuthAdapter(list, childFragmentManager, lifecycle)
                binding.viewPagerAuth.adapter = adapter
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpResponse.collectLatest {
                if (it.navigateToAddAddress) {
                    findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToAddAddressSignUpFragment())
                    viewModel.navigateToAddAddressDone()
                }
            }
        }



        onPageSelected = getOnPageChangeCallBack()
        binding.tabAuthFamily.addOnTabSelectedListener(getTabOnSelectedListener())
        binding.viewPagerAuth.registerOnPageChangeCallback(onPageSelected)


        binding.btnArrowBackSignInFamily.setOnClickListener {
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
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPagerAuth.currentItem = tab.position
                    viewModel.setStatePage(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        }
    }

    private fun getOnPageChangeCallBack(): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabAuthFamily.selectTab(binding.tabAuthFamily.getTabAt(position))
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
                binding.tabAuthFamily.selectTab(binding.tabAuthFamily.getTabAt(0))
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