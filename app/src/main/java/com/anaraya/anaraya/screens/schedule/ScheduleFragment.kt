package com.anaraya.anaraya.screens.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentScheduleBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.address.add_address.CompanyAddressAdapter
import com.anaraya.anaraya.util.gone
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel by viewModels<ScheduleViewModel>({ this })

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button

    private var allCompaniesId = -1
    private var allCompanies = emptyList<Int>()

    private var allGovernorateId = -1
    private var allGovernorate = emptyList<String>()

    private var allCompanyAddressId = -1
    private var allCompanyAddress = emptyList<String>()
    private lateinit var adapterAllCompanies : ArrayAdapter<String>
    private lateinit var adapterAllGovernorate : ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)


        var adapter = CompanyAddressAdapter()

        lifecycleScope.launch {
            viewModel.scheduleUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToBack) {
                    findNavController().popBackStack()
                    viewModel.navigateToBackDone()
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
                        binding.edtCompanyDelivery.setAdapter(adapterAllCompanies)
                    }
                }
                if (it.allGovernorate.isNotEmpty()) {
                    if (allGovernorate.isEmpty()) {
                        allGovernorate = it.allGovernorate
                        adapterAllGovernorate.addAll(it.allGovernorate)
                        binding.edtLineGovernmentDelivery.setAdapter(adapterAllGovernorate)
                    }
                }
                if (it.allCompanyAddresses != null) {
                    binding.recyclerDelivery.adapter = adapter
                    it.allCompanyAddresses.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }

        binding.edtCompanyDelivery.setOnItemClickListener { _, _, position, _ ->
            if (allCompaniesId != position) {
                allCompaniesId = position

                allGovernorate = emptyList()
                allGovernorateId = -1

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1
                adapterAllGovernorate.clear()
                binding.edtLineGovernmentDelivery.setAdapter(ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address))
                binding.edtLineGovernmentDelivery.text.clear()
                adapter = CompanyAddressAdapter()
                binding.recyclerDelivery.adapter = adapter

                viewModel.getAllGovernorateByCompanyId(allCompanies[position])
            }
        }
        binding.edtLineGovernmentDelivery.setOnItemClickListener { _, _, position, _ ->
            if (allGovernorateId != position) {
                allGovernorateId = position

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1

                adapter = CompanyAddressAdapter()
                binding.recyclerDelivery.adapter = adapter
                viewModel.getAllCompanyAddresses(
                    allCompanies[allCompaniesId],
                    allGovernorate[position]
                )
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

    override fun onStart() {
        super.onStart()
        val cardSearch = requireActivity().findViewById<CardView>(R.id.cardSearch_homeActivity)
        val bottomNav = requireActivity().findViewById<ChipNavigationBar>(R.id.bottomNavHome)
        cardSearch.gone()
        bottomNav.gone()
        binding.edtCompanyDelivery.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtLineGovernmentDelivery.setDropDownBackgroundResource(R.drawable.shape_drop_down)

        adapterAllCompanies = ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterAllGovernorate = ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

}