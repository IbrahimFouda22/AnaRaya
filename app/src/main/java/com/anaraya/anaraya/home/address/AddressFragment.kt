package com.anaraya.anaraya.home.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentAddressBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.address.adapter.AddressAdapter
import com.anaraya.anaraya.home.address.interaction.AddressInteraction
import com.anaraya.anaraya.home.shared_data.AddressUiState
import com.anaraya.anaraya.util.showToolBar
import com.anaraya.anaraya.util.visible
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment(), AddressInteraction {

    private val navArgs by navArgs<AddressFragmentArgs>()
    private val viewModel by viewModels<AddressViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentAddressBinding
    private lateinit var toolBar: MaterialToolbar
    private var navigatedAddress: AddressUiState? = null
    private var size = 0
    //private var allList = mutableListOf<AddressUiStateData>()

    //private var addressId = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        toolBar = requireActivity().findViewById(R.id.toolBarActivity)

        toolBar.inflateMenu(R.menu.menu_address)
        toolBar.setNavigationIcon(R.drawable.ic_nav_back)
        toolBar.visible()

        val adapter = AddressAdapter(this)
        binding.recyclerAddress.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addressesUiState.collectLatest {
                if (it.navigateToAddAddress) {
                    findNavController().navigate(
                        AddressFragmentDirections.actionAddressFragmentToAddAddressFragment(
                            false
                        )
                    )
                    viewModel.navigateToAddAddressDone()
                }
                if (it.navigateToEditAddress) {
                    findNavController().navigate(
                        AddressFragmentDirections.actionAddressFragmentToEditAddressFragment(
                            navigatedAddress!!
                        )
                    )
                    viewModel.navigateToEditAddressDone()
                }
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.errorChangeDefaultAddress != null) {
                    viewModel.getAllData()
                }
                if (it.changeAddressUiState != null) {
//                    Toast.makeText(context, it.changeAddressUiState.message, Toast.LENGTH_SHORT)
//                        .show()
                    if (it.isSucceed) {
                        if (navArgs.fromCart) {
                            findNavController().popBackStack()
                        } else {
                            viewModel.getAllData()
                            sharedViewModel.getUserMyInfo()
                        }
                    }
                }
                size = it.allAddressesUiState.size
                adapter.submitList(it.allAddressesUiState)
            }
        }

        lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.getAddresses) {
                    viewModel.getAllData()
                    sharedViewModel.getAddressesDone()
                }
            }
        }


        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }

        toolBar.setOnMenuItemClickListener(onMenuItemClickListener())
        return binding.root
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolBar.menu.clear()
        binding.unbind()
    }

    private fun onMenuItemClickListener() = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.btnAddAddressMenu -> {
                viewModel.navigateToAddAddress()
                true
            }

            else -> true
        }
    }

    override fun onClickChange(address: AddressUiState) {
        navigatedAddress = address
        viewModel.navigateToEditAddress()
    }

    override fun onClickSwitch(addressId: String, isUserAddress: Boolean) {
        //Toast.makeText(requireContext(), "$isUserAddress", Toast.LENGTH_SHORT).show()
        if (addressId != "-1")
            viewModel.changeDefaultAddress(addressId, isUserAddress)

    }

    override fun onClickDelete(addressId: String) {
        viewModel.deleteAddress(addressId)
    }

}