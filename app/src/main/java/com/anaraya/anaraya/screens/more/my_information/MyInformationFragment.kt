package com.anaraya.anaraya.screens.more.my_information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentMyInformationBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyInformationFragment : Fragment() {
    private lateinit var binding: FragmentMyInformationBinding
    private val sharedViewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<MyInformationViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyInformationBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        lifecycleScope.launch {
            viewModel.myInfoUiState.collectLatest {
                if(it.error!=null){
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToAddresses) {
                    findNavController().navigate(MyInformationFragmentDirections.actionMyInformationFragmentToAddressFragment())
                    viewModel.navigateToAddressesDone()
                }
                if (it.navigateToName) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToEditInformationFragment(
                            "name",
                            it.profileUiState!!.name
                        )
                    )
                    viewModel.navigateToNameDone()
                }
                if (it.navigateToEmail) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToEditInformationFragment(
                            "email",
                            it.profileUiState!!.email
                        )
                    )
                    viewModel.navigateToEmailDone()
                }
                if (it.navigateToPhone) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToEditInformationFragment(
                            "phone", it.profileUiState!!.phoneNumber
                        )
                    )
                    viewModel.navigateToPhoneDone()
                }
                if (it.navigateToVerifyPhone) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToOTPFragment()
                    )
                    viewModel.navigateToVerifyPhoneDone()
                }
                if (it.navigateToDOB) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToEditDOBFragment(
                            it.profileUiState!!.dateOfBirth
                        )
                    )
                    viewModel.navigateToDOBDone()
                }
                if (it.navigateToGender) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToEditGenderFragment(
                            it.profileUiState!!.gender
                        )
                    )
                    viewModel.navigateToGenderDone()
                }
                if (it.navigateToChangePass) {
                    findNavController().navigate(
                        MyInformationFragmentDirections.actionMyInformationFragmentToChangePassFragment()
                    )
                    viewModel.navigateToChangePassDone()
                }
                if(it.isSendOtpSucceed){
                    if (it.msgSendOtp != null){
                        Toast.makeText(requireContext(), it.msgSendOtp, Toast.LENGTH_SHORT).show()
                        viewModel.resetMsg()
                        viewModel.navigateToVerifyPhone()
                    }
                }
            }
        }
        lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if(it.getUserMyInfo) {
                    viewModel.getAllData()
                    sharedViewModel.getUserMyInfoDone()
                }
            }
        }
        binding.txtMobileNumberInfoVerify.setOnClickListener {
            viewModel.sendPhoneOtp()
        }
        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }
        return binding.root
    }
    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }
    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(), true)
        showBottomNavBar(requireActivity(), false)
    }
}