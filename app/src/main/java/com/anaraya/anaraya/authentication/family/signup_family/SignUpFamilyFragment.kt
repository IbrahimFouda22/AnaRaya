package com.anaraya.anaraya.authentication.family.signup_family

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import com.anaraya.anaraya.databinding.FragmentSignUpFamilyBinding
import com.anaraya.anaraya.screens.activity.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFamilyFragment : Fragment() {
    private lateinit var binding: FragmentSignUpFamilyBinding
    private val sharedViewModel by viewModels<MainActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<AuthFamilyViewModel>({ requireActivity() })

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpFamilyBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        btnBack = requireActivity().findViewById(R.id.btnBackMainActivity)
        btnReload = requireActivity().findViewById(R.id.btnReloadMain)

        binding.btnArrowNextSignUpFamily.setOnClickListener {
            setStateSignUpPlus()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpResponse.collectLatest {
                if (it.token != null) {
                    saveUser(it.token,it.refreshToken)
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()
                }
                if (it.auth) {
                    setStateSignUpPlus(true)
                }
                if (it.isOtpSent) {
                    setStateSignUpPlus(auth = true, isMobileCorrect = true)
                }
                if (it.isOtpCorrect) {
                    setStateSignUpPlus(isOtpCorrect = true)
                }
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet)) Toast.makeText(
                        context,
                        it.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!it.isSucceedSignUp) Toast.makeText(
                    context,
                    it.messageSignUp,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.edtDOBSignUpFamily.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.edtDOBSignUpFamily.showSoftInputOnFocus = false
                val d = DatePickerDialog(requireContext())
                d.setCancelable(false)
                d.show()
                d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val day =
                        if (d.datePicker.dayOfMonth.toString().length == 1) "0${d.datePicker.dayOfMonth}" else d.datePicker.dayOfMonth
                    val month =
                        if (d.datePicker.month.toString().length == 1) "0${d.datePicker.month}" else d.datePicker.month
                    var m = month.toString().toInt()
                    m++
                    binding.edtDOBSignUpFamily.setText("${d.datePicker.year}-$m-$day")
                    binding.edtDOBSignUpFamily.clearFocus()
                    d.cancel()
                }
                d.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    binding.edtDOBSignUpFamily.clearFocus()
                    d.cancel()
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

    private fun setStateSignUpPlus(
        auth: Boolean = false,
        isMobileCorrect: Boolean = false,
        isOtpCorrect: Boolean = false,
    ) {
        var num = viewModel.stateSignUp.value
        when (num) {
            1 -> {
                val boolean = viewModel.validateRayaNationalId(
                    binding.edtRayaIdNumSignUpFamily.text.toString(),
                )
                if (boolean) {
                    if (auth) {
                        viewModel.setStateSignUpUpdate(++num)
                    } else {
                        viewModel.checkAuth(
                            binding.edtRayaIdNumSignUpFamily.text.toString(),
                        )
                    }
                }
            }

            2 -> {

                if (auth && isMobileCorrect) {
                    viewModel.setStateSignUpUpdate(++num)
                } else {
                    viewModel.sendVerifyCode(
                        binding.edtRayaIdNumSignUpFamily.text.toString(),
                        binding.edtPhoneNumberSignUpFamily.text.toString(),
                    )
                }

            }

            3 -> {
                if (isOtpCorrect) {
                    viewModel.setStateSignUpUpdate(++num)
                } else {
                    viewModel.checkVerifyCode(
                        binding.edtRayaIdNumSignUpFamily.text.toString(),
                        binding.edtPhoneNumberSignUpFamily.text.toString(),
                        binding.edtOtpFamilySignUpFamily.text.toString()
                    )
                }
            }

            else -> signUp()
        }
    }

    private fun saveUser(token: String, refreshToken: String?) {
        sharedPreferences.edit().putString("token", token).apply()
        sharedPreferences.edit().putString("refreshToken", refreshToken).apply()
        sharedPreferences.edit().putInt(getString(R.string.productsinbasket), 0).apply()
        sharedPreferences.edit().putBoolean("auth", false).apply()
        sharedPreferences.edit()
            .putString("rayaId", binding.edtRayaIdNumSignUpFamily.text.toString()).apply()
        sharedPreferences.edit()
            .putString("password", binding.edtPasswordSignUpFamily.text.toString()).apply()
        sharedPreferences.edit().putBoolean("isFamily", true).apply()
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        signUp()
        sharedViewModel.reloadClickDone()
    }

    private fun signUp() {
        viewModel.signUp(
            binding.edtRayaIdNumSignUpFamily.text.toString(),
            binding.edtPhoneNumberSignUpFamily.text.toString(),
            binding.edtOtpFamilySignUpFamily.text.toString(),
            binding.edtNameSignUpFamily.text.toString(),
            binding.edtEmailSignUpFamily.text.toString(),
            binding.edtPasswordSignUpFamily.text.toString(),
            binding.edtDOBSignUpFamily.text.toString(),
            if (binding.radioGroupGenderSignUpFamily.checkedRadioButtonId == binding.radioMaleSignUpFamily.id) 1
            else if ((binding.radioGroupGenderSignUpFamily.checkedRadioButtonId == binding.radioFemaleSignUp.id)) 2
            else null,
            viewModel.signUpResponse.value.addressUiStateData?.addressLabel,
            viewModel.signUpResponse.value.addressUiStateData?.governorate,
            viewModel.signUpResponse.value.addressUiStateData?.district,
            viewModel.signUpResponse.value.addressUiStateData?.address,
            viewModel.signUpResponse.value.addressUiStateData?.street,
            viewModel.signUpResponse.value.addressUiStateData?.building,
            viewModel.signUpResponse.value.addressUiStateData?.landmark,
        )
    }
}