package com.anaraya.anaraya.screens.address.edit_address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentEditAddressBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditAddressFragment : Fragment() {

    private lateinit var binding: FragmentEditAddressBinding
    private val navArgs by navArgs<EditAddressFragmentArgs>()
    @Inject
    lateinit var factory: EditAddressViewModel.EditAddressAssistedFactory
    private val viewModel by viewModels<EditAddressViewModel> {
        EditAddressViewModel.createFactory(factory, navArgs.address)
    }

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditAddressBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        lifecycleScope.launch {
            viewModel.editAddressUiState.collectLatest {
                if (!it.error.isNullOrEmpty()){
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if(it.editAddressUiState!=null){
                    Toast.makeText(context, it.editAddressUiState, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.addressLabel.collectLatest {
                viewModel.updateVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.governorate.collectLatest {
                viewModel.updateVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.district.collectLatest {
                viewModel.updateVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.address.collectLatest {
                viewModel.updateVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.street.collectLatest {
                viewModel.updateVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.building.collectLatest {
                viewModel.updateVisibility()
            }
        }
        lifecycleScope.launch {
            viewModel.landmark.collectLatest {
                viewModel.updateVisibility()
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
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

}