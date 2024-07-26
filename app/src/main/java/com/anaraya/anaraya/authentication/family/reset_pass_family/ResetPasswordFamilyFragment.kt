package com.anaraya.anaraya.authentication.family.reset_pass_family

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.MainActivityViewModel
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.family.AuthFamilyViewModel
import com.anaraya.anaraya.databinding.FragmentResetPasswordFamilyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFamilyFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordFamilyBinding
    private val viewModel by viewModels<AuthFamilyViewModel>({ requireActivity() })
    private val sharedViewModel by viewModels<MainActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordFamilyBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        btnBack = requireActivity().findViewById(R.id.btnBackMainActivity)
        btnReload = requireActivity().findViewById(R.id.btnReloadMain)
        binding.btnArrowNextResetPasswordFamily.setOnClickListener {
            setStateResetPlus()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resetPassResponse.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.isSucceedForgetPass) {
                    setStateResetPlus(true)
                    Toast.makeText(context, it.messageForgetPass, Toast.LENGTH_LONG)
                        .show()
                }
                if (!it.isSucceedForgetPass) {
                    if (it.messageForgetPass != null)
                        Toast.makeText(context, it.messageForgetPass, Toast.LENGTH_SHORT).show()
                }
                if (it.isSucceedCheckCode) {
                    setStateResetPlus(forgetPassCheckCode = true)
                }
                if (!it.isSucceedCheckCode) {
                    if (it.messageCheckCode != null)
                        Toast.makeText(context, it.messageCheckCode, Toast.LENGTH_SHORT).show()
                }
                if (it.isSucceedResetPass) {
                    Toast.makeText(context, it.messageResetPass, Toast.LENGTH_SHORT).show()
                }
                if (!it.isSucceedResetPass) {
                    if (it.messageResetPass != null)
                        Toast.makeText(context, it.messageResetPass, Toast.LENGTH_SHORT).show()
                }
            }
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
        val rayaId = binding.edtRayaIdNumResetPasswordFamily.text.toString()
        val code = binding.edtVerificationResetFamily.text.toString()
        val newPass = binding.edtNewPassResetPasswordFamily.text.toString()
        sharedViewModel.reloadClick()
        viewModel.resetPass(
            rayaId, code, newPass
        )
        sharedViewModel.reloadClickDone()
    }
    private fun setStateResetPlus(
        forgetPass: Boolean = false,
        forgetPassCheckCode: Boolean = false
    ) {
        var num = viewModel.stateResetPass.value
        val rayaId = binding.edtRayaIdNumResetPasswordFamily.text.toString()
        val code = binding.edtVerificationResetFamily.text.toString()
        val newPass = binding.edtNewPassResetPasswordFamily.text.toString()

        when (num) {
            1 -> {
                val boolean = viewModel.validateRayaNationalId(
                    rayaId,
                )
                if (boolean) {
                    if (forgetPass) {
                        viewModel.setStateResetPassUpdate(++num)
                    } else {
                        viewModel.forgetPass(
                            rayaId,
                        )
                    }
                }
            }

            2 -> {
                if (forgetPassCheckCode) {
                    viewModel.setStateResetPassUpdate(++num)
                    viewModel.resetStates()
                } else {
                    viewModel.forgetPassCheckPass(
                        rayaId,
                        code
                    )
                }
            }

            else -> {
                viewModel.resetPass(
                    rayaId, code, newPass
                )
            }
        }
    }
}