package com.anaraya.anaraya.screens.services.store.service.my_items

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
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentMyItemsServicesBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.adapter.StoreItemServiceAdapter
import com.anaraya.anaraya.screens.services.store.interaction.ItemsServicesInteraction
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyItemsServicesFragment : Fragment(), ItemsServicesInteraction {
    private lateinit var binding: FragmentMyItemsServicesBinding
    private val viewModel by viewModels<MyItemsServicesViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var statusId: Int = 1
    private lateinit var adapter: StoreItemServiceAdapter
    private var mutableMap: MutableMap<Int, Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyItemsServicesBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = StoreItemServiceAdapter(this)
        binding.recyclerStoreItemsServices.adapter = adapter
        lifecycleScope.launch {
            viewModel.myItemsUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.itemsServices != null) {
                    it.itemsServices.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
                if (it.requestToDeleteMsg != null) {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                }
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }

        adapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                viewModel.showLoading(false)
                if (it.error.message == getString(R.string.not_found)) {
                    if (adapter.snapshot().isEmpty())
                        viewModel.setError(it.error.message)
                } else {
                    viewModel.setError(it.error.message)
                }
            }
        }

        binding.chipStatusServices.setOnCheckedStateChangeListener(onChipSelectedListener())

        btnBack.setOnClickListener {
            reload()
        }

        btnReload.setOnClickListener {
            reload()
        }

        return binding.root
    }
    private fun onChipSelectedListener(): ChipGroup.OnCheckedStateChangeListener {
        return ChipGroup.OnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                if (checkedIds[0] != statusId) {
                    viewModel.showLoading(true)
                    statusId = checkedIds[0]
                    viewModel.getItemsServices(mutableMap[checkedIds[0]]!!)
                } else {
                    binding.chipStatusServices.check(statusId)
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        mutableMap[binding.chipPendingServices.id] = 1
        mutableMap[binding.chipAcceptedServices.id] = 2
        mutableMap[binding.chipRejectedServices.id] = 3
        mutableMap[binding.chipSoldServices.id] = 4
        mutableMap[binding.chipCancelServices.id] = 5
        statusId = binding.chipPendingServices.id
        binding.chipStatusServices.check(statusId)
        viewModel.getItemsServices(mutableMap[statusId]!!)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData(statusId)
        //binding.chipStatus.check(statusId)
        sharedViewModel.reloadClickDone()
    }

    override fun onClick(item: ProductStoreItemServiceState?, itemId: Int, userAction: Int) {
//        when (statusId) {
//            binding.chipPendingServices.id -> showBottomSheet(item,itemId, userAction, 1)
//            binding.chipAcceptedServices.id -> showBottomSheet(item,itemId, userAction, 2)
//        }
    }
}