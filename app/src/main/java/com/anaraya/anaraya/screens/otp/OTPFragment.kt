package com.anaraya.anaraya.screens.otp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentOTPBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OTPFragment : Fragment() {
    private val sharedViewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<OTPViewModel>()
    private lateinit var binding: FragmentOTPBinding
    private var code = "0"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.otpUiState.collectLatest {
                if(!it.msgUpdate.isNullOrEmpty()) {
                    if (it.isSucceed){
                        Toast.makeText(context, it.msgUpdate, Toast.LENGTH_SHORT).show()
                        sharedViewModel.getUserMyInfo()
                        sharedViewModel.getUserMoreInfo()
                        findNavController().popBackStack()
                    }
                    else{
                        Toast.makeText(context, it.msgUpdate, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        //binding.otpView.requestFocus()
        binding.otpView.setOtpCompletionListener {
            code = it
            viewModel.verifyPhone(code)
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
        viewModel.getAllData(code)
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(), true)
        binding.otpView.requestFocus()
        val inputMethodManager:InputMethodManager =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.otpView,InputMethodManager.SHOW_IMPLICIT)
    }
}