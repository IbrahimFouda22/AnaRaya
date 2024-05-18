package com.anaraya.anaraya.authentication.resetpassword

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.authentication.AuthViewModel
import com.anaraya.anaraya.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel by viewModels<AuthViewModel>({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.btnArrowNextResetPassword.setOnClickListener {
            setStateResetPlus()
        }

        lifecycleScope.launch {
            viewModel.resetPassResponse.collectLatest {
                if (it.error != null) {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.isSucceedForgetPass) {
                    setStateResetPlus(true)
                    Toast.makeText(context, "The Code is ${it.messageCode}", Toast.LENGTH_LONG + 4)
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

        return binding.root
    }

    private fun setStateResetPlus(
        forgetPass: Boolean = false,
        forgetPassCheckCode: Boolean = false
    ) {
        var num = viewModel.stateResetPass.value
        Log.d("ResetNumber", "$num")
        val rayaId = binding.edtRayaIdNumResetPassword.text.toString()
        val nationalId = binding.edtNationalIdNumResetPassword.text.toString()
        val code = binding.edtVerificationReset.text.toString()
        val newPass = binding.edtNewPassResetPassword.text.toString()

        when (num) {
            1 -> {
                val boolean = viewModel.validateRayaNationalId(
                    rayaId,
                    nationalId
                )
                if (boolean) {
                    if (forgetPass) {
                        viewModel.setStateResetPassUpdate(++num)
                    } else {
                        viewModel.forgetPass(
                            rayaId,
                            nationalId
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
                        nationalId,
                        code
                    )
                }
            }

            else -> {
//                val boolean = viewModel.validateRayaNationalId(
//                    binding.edtRayaIdNumSignUp.text.toString(),
//                    binding.edtNationalIdNumSignUp.text.toString()
//                )
                viewModel.resetPass(
                    rayaId, nationalId, code, newPass
                )
            }
        }
    }


}