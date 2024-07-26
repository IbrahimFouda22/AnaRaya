package com.anaraya.anaraya.screens.address.add_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentAddAddressBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddressFragment : Fragment() {

    private val navArgs by navArgs<AddAddressFragmentArgs>()
    private lateinit var binding: FragmentAddAddressBinding

    //    private lateinit var bindingApartment: ApartmentDetailsStubBinding
//    private lateinit var bindingOffice: OfficeDetailsStubBinding
    private val viewModel by viewModels<AddAddressViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var toolBar: MaterialToolbar
    private var allCompaniesId = -1
    private var allCompanies = emptyList<Int>()

    private var allGovernorateId = -1
    private var allGovernorate = emptyList<String>()

    private var allCompanyAddressId = -1
    private var allCompanyAddress = emptyList<String>()
    private var allCompanyAddressListId = emptyList<String>()
    private var allCompanyAddressListInFav = emptyList<Boolean>()

    private lateinit var adapterAllCompanies: ArrayAdapter<String>
    private lateinit var adapterAllGovernorate: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapter = CompanyAddressAdapter()

        lifecycleScope.launch {
            viewModel.addAddressUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.addAddressUiState != null) {
                    Toast.makeText(context, it.addAddressUiState, Toast.LENGTH_SHORT).show()
                    if (it.isSucceed) {
                        if (!navArgs.fromCart) {
                            sharedViewModel.getAddresses()
                        } else
                            sharedViewModel.getCart()
                        findNavController().popBackStack()
                    }
                }
                if (it.allCompanies.isNotEmpty()) {
                    if (allCompanies.isEmpty()) {
                        allCompanies = it.allCompanies.map { data ->
                            data.id
                        }
                        val m = it.allCompanies.map { data ->
                            data.name
                        }
                        adapterAllCompanies.addAll(m)
                        binding.edtLineOfBusiness.setAdapter(adapterAllCompanies)
                    }
                }
                if (it.allGovernorate.isNotEmpty()) {
                    if (allGovernorate.isEmpty()) {
                        allGovernorate = it.allGovernorate
                        adapterAllGovernorate.addAll(it.allGovernorate)
                        binding.edtLineGovernorate.setAdapter(adapterAllGovernorate)
                    }
                }
                if (it.allCompanyAddresses != null) {
                    //adapter.submitData(PagingData.from(emptyList()))
                    it.allCompanyAddresses.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
                if (it.allCompanyAddresses == null) {
                    val adapterAllCompanyAddress =
                        ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                    binding.edtLineNameOfTheCompany.setAdapter(adapterAllCompanyAddress)
                }
            }
        }

        binding.edtLineOfBusiness.setOnItemClickListener { _, _, position, _ ->
            if (allCompaniesId != position) {
                allCompaniesId = position

                allGovernorate = emptyList()
                allGovernorateId = -1

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1

                adapterAllGovernorate.clear()
                binding.edtLineGovernorate.setAdapter(ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address))
                binding.edtLineGovernorate.text.clear()

                binding.edtLineNameOfTheCompany.text.clear()
                //adapter = CompanyAddressAdapter()

                viewModel.getAllGovernorateByCompanyId(allCompanies[position])
            }
        }
        binding.edtLineGovernorate.setOnItemClickListener { _, _, position, _ ->
            if (allGovernorateId != position) {
                allGovernorateId = position

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1
                binding.edtLineNameOfTheCompany.text.clear()
                viewModel.getAllCompanyAddresses(
                    allCompanies[allCompaniesId],
                    allGovernorate[position]
                )
            }
        }

        binding.edtLineNameOfTheCompany.setOnItemClickListener { _, _, position, _ ->
            allCompanyAddressId = position
            //viewModel.getAllCompanyAddresses(allCompaniesId,allGovernorate[position])
        }


        // create normal paging adapter and listen for data and fill array adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.refresh is LoadState.Loading)
                if (it.refresh is LoadState.NotLoading) {
                    allCompanyAddress = adapter.snapshot().items.map { data ->
                        data.office
                    }
                    allCompanyAddressListId = adapter.snapshot().items.map { data ->
                        data.id
                    }
                    allCompanyAddressListInFav = adapter.snapshot().items.map { data ->
                        data.addedToFavouriteOrNot
                    }
                    val adapterAllCompanyAddress =
                        ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                    binding.edtLineNameOfTheCompany.setAdapter(adapterAllCompanyAddress)
                    adapterAllCompanyAddress.addAll(allCompanyAddress)

                }
            }
        }
        binding.btnSaveOffice.setOnClickListener {
            addCompanyAddress()
        }
        binding.btnSave.setOnClickListener {
            addUserAddress()
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
        sharedViewModel.reloadClickDone()
    }

    private fun addUserAddress() {
        viewModel.addUserAddress(
            binding.edtAddressLabel.text.toString(),
            binding.edtGovernorate.text.toString(),
            binding.edtDistrict.text.toString(),
            binding.edtAddress.text.toString(),
            binding.edtStreet.text.toString(),
            binding.edtBuilding.text.toString(),
            binding.edtLandmark.text.toString(),
        )
    }

    private fun addCompanyAddress() {
        if (allCompaniesId == -1)
            viewModel.addCompanyAddress(null, null, null)
        else if (allGovernorateId == -1)
            viewModel.addCompanyAddress("s", null, null)
        else if (allCompanyAddressId == -1)
            viewModel.addCompanyAddress("s", "s", null)
        else {
            if (allCompanyAddressListInFav[allCompanyAddressId])
                Toast.makeText(
                    requireContext(),
                    getString(R.string.this_address_already_in_your_addresses),
                    Toast.LENGTH_SHORT
                ).show()
            else
                viewModel.addCompanyAddress("s", "s", allCompanyAddressListId[allCompanyAddressId])
        }
    }

    override fun onStart() {
        super.onStart()
        toolBar = requireActivity().findViewById(R.id.toolBarActivity)
        toolBar.setNavigationIcon(R.drawable.ic_nav_back)
        showToolBar(requireActivity(), true)
        binding.edtLineOfBusiness.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtLineGovernorate.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtLineNameOfTheCompany.setDropDownBackgroundResource(R.drawable.shape_drop_down)

        adapterAllCompanies =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterAllGovernorate = ArrayAdapter<String>(
            requireContext(), R.layout.layout_item_company_address
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}