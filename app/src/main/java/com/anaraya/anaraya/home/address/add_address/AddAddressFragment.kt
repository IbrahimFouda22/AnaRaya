package com.anaraya.anaraya.home.address.add_address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentAddAddressBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddressFragment : Fragment() {

    private val navArgs by navArgs<AddAddressFragmentArgs>()
    private lateinit var binding: FragmentAddAddressBinding
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
    private var allCompanyAddressListInFav= emptyList<Boolean>()

    //    private var apartment = false
//    private var office = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        binding.edtLineOfBusiness.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtLineGovernorate.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtLineNameOfTheCompany.setDropDownBackgroundResource(R.drawable.shape_drop_down)

        val adapterAllCompanies =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        binding.edtLineOfBusiness.setAdapter(adapterAllCompanies)

//        val adapterAllGovernorate =
//            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
//        binding.edtLineGovernorate.setAdapter(adapterAllGovernorate)


        val adapter = CompanyAddressAdapter()

        lifecycleScope.launch {
            viewModel.addAddressUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.addAddressUiState != null) {
                    Toast.makeText(context, it.addAddressUiState, Toast.LENGTH_SHORT).show()
                    if(it.isSucceed){
                        if (!navArgs.fromCart) {
                            sharedViewModel.getAddresses()
                        }else
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
                    }
//                    binding.edtLineOfBusiness.adapter = ArrayAdapter<String>(requireContext(),it.allCompanies.size)
                }
                if (it.allGovernorate.isNotEmpty()) {
                    if (allGovernorate.isEmpty()) {
                        allGovernorate = it.allGovernorate
                        binding.edtLineGovernorate.text.clear()
                        binding.edtLineNameOfTheCompany.text.clear()
                        //adapterAllGovernorate.clear()
                        val adapterAllGovernorate =
                            ArrayAdapter<String>(
                                requireContext(),
                                R.layout.layout_item_company_address
                            )
                        binding.edtLineGovernorate.setAdapter(adapterAllGovernorate)
                        adapterAllGovernorate.addAll(it.allGovernorate)
                        adapter.submitData(PagingData.empty())
                        val adapterAllCompanyAddress =
                            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                        binding.edtLineNameOfTheCompany.setAdapter(adapterAllCompanyAddress)
                    }
                }
                /*if (it.allCompanyAddresses == null) {
                    adapter.submitData(PagingData.empty())
                    val adapterAllCompanyAddress =
                        ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                    binding.edtLineNameOfTheCompany.setAdapter(adapterAllCompanyAddress)
                }*/
                if (it.allCompanyAddresses != null) {
                    //adapter.submitData(PagingData.from(emptyList()))
                    it.allCompanyAddresses.collectLatest { data ->
                        adapter.submitData(data)
                    }
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
                //adapter = CompanyAddressAdapter()

                viewModel.getAllGovernorateByCompanyId(allCompanies[position])
            }
        }
        binding.edtLineGovernorate.setOnItemClickListener { _, _, position, _ ->
            if (allGovernorateId != position) {
                allGovernorateId = position

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1

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


        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.refresh is LoadState.Loading)
                if (it.refresh is LoadState.NotLoading) {
                    allCompanyAddress = adapter.snapshot().items.map { data ->
                        data.companyName
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

//        lifecycleScope.launch {
//            viewModel.addressTypeState.collectLatest {
//                apartment = it.apartment
//                office = it.office
//            }
//        }
        //var arrayAdapter = ArrayAdapter<AddressUiState>


        binding.btnSave.setOnClickListener {
            addUserAddress()
        }

        binding.btnSaveOffice.setOnClickListener {
            addCompanyAddress()
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
            viewModel.addCompanyAddress("s",null,null)

        else if (allCompanyAddressId == -1)
            viewModel.addCompanyAddress("s","s",null)
        else {
            if(allCompanyAddressListInFav[allCompanyAddressId])
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        if(navArgs.fromCart)
//            sharedViewModel.getCheckOut()
//        else
//            sharedViewModel.getCheckOutDone()
        binding.unbind()
    }

}