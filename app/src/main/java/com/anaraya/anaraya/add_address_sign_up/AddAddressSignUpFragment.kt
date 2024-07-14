package com.anaraya.anaraya.add_address_sign_up

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.MainActivityViewModel
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.user.AuthViewModel
import com.anaraya.anaraya.databinding.FragmentAddAddressSignUpBinding
import com.anaraya.anaraya.util.showMainToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddAddressSignUpFragment : Fragment() {
    private lateinit var binding: FragmentAddAddressSignUpBinding
    private val sharedViewModel by viewModels<AuthViewModel>({ requireActivity() })
    private val sharedHomeViewModel by viewModels<MainActivityViewModel>({ requireActivity() })

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var factory: AddAddressSignUpViewModel.AssistedFactory
    private val viewModel by viewModels<AddAddressSignUpViewModel> {
        AddAddressSignUpViewModel.createProvider(
            sharedViewModel.signUpResponse.value.addressUiStateData,
            factory
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressSignUpBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addAddressUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedHomeViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.isSucceed) {
                    sharedViewModel.setAddress(viewModel.addAddressUiState.value.addressUiStateData)
                    showMainToolBar(requireActivity(), false)
                    findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showMainToolBar(requireActivity(), true)
        sharedPreferences.edit().putBoolean(getString(R.string.signupstate), true).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showMainToolBar(requireActivity(), false)
    }
}