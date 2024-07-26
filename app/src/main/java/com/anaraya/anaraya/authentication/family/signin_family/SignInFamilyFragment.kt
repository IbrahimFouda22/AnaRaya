package com.anaraya.anaraya.authentication.family.signin_family

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.MainActivityViewModel
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.family.AuthFamilyViewModel
import com.anaraya.anaraya.databinding.FragmentSignInFamilyBinding
import com.anaraya.anaraya.screens.activity.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFamilyFragment : Fragment() {
    private lateinit var binding: FragmentSignInFamilyBinding
    private val sharedViewModel by viewModels<MainActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<AuthFamilyViewModel>({ requireActivity() })
    private var contactNumber: String? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInFamilyBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        btnBack = requireActivity().findViewById(R.id.btnBackMainActivity)
        btnReload = requireActivity().findViewById(R.id.btnReloadMain)

        binding.btnArrowNextSignInFamily.setOnClickListener {
            setStateSignInPlus()
        }

        binding.txtBtnForgotPassFamily.setOnClickListener {
            viewModel.setStateViewPagerUpdate(1)
        }
        lifecycleScope.launch {
            viewModel.signInResponse.collectLatest {
                if (it.token != null) {
                    saveUser(it.token,it.productsInBasket)
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()
                }

                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (!it.isSucceedSignIn) {
                    Toast.makeText(context, it.messageSignIn, Toast.LENGTH_SHORT).show()
                }
                if (it.contactNumber != null) {
                    contactNumber = it.contactNumber
                }
            }
        }

        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }

        binding.btnHelpSignInFamily.setOnClickListener {
            if(contactNumber == null)
                viewModel.getContactNumber()
            else
                showDialog()
        }
        return binding.root
    }

    private fun setStateSignInPlus() {
        var num = viewModel.stateSignIn.value
        when (num) {
            1 -> viewModel.setStateSignInUpdate(++num)
            else -> signIn(
                binding.edtRayaIdNumSignInFamily.text.toString(),
                binding.edtPasswordSignInFamily.text.toString()
            )
        }
    }

    private fun signIn(rayaId: String?, password: String?) {
        viewModel.signIn(rayaId, password)
    }

    private fun saveUser(token: String, productsInBasket: Int) {
        sharedPreferences.edit().putString("token", token).apply()
        sharedPreferences.edit().putInt(getString(R.string.productsinbasket), productsInBasket).apply()
        sharedPreferences.edit().putBoolean("auth", false).apply()
        sharedPreferences.edit().putString("rayaId", binding.edtRayaIdNumSignInFamily.text.toString())
            .apply()
        sharedPreferences.edit().putString("password", binding.edtPasswordSignInFamily.text.toString())
            .apply()
        sharedPreferences.edit().putBoolean("isFamily", true)
            .apply()
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        signIn(
            binding.edtRayaIdNumSignInFamily.text.toString(),
            binding.edtPasswordSignInFamily.text.toString()
        )
        sharedViewModel.reloadClickDone()
    }

    private fun showDialog(){
        AlertDialog.Builder(requireContext()).setCancelable(false)
            .setTitle(getString(R.string.contact_number)).setMessage(contactNumber)
            .setNeutralButton(
                requireContext().getString(R.string.ok)
            ) { dialog, _ ->
                dialog.cancel()
            }.show()
    }

}