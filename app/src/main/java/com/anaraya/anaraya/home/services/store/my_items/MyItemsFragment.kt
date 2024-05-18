package com.anaraya.anaraya.home.services.store.my_items

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
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentMyItemsBinding
import com.anaraya.anaraya.databinding.LayoutBottomSheetPendingBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.services.store.adapter.StoreProductItemServiceAdapter
import com.anaraya.anaraya.home.services.store.interaction.ItemsServiceInteraction
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyItemsFragment(private val isProducts: Boolean) : Fragment(), ItemsServiceInteraction {
    private lateinit var binding: FragmentMyItemsBinding
    private val viewModel by viewModels<MyItemsViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var statusId: Int = 1
    private lateinit var adapter: StoreProductItemServiceAdapter
    private var mutableMap: MutableMap<Int, Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyItemsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = StoreProductItemServiceAdapter(this)
        binding.recyclerStoreItemsService.adapter = adapter
        lifecycleScope.launch {
            viewModel.myItemsUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
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

        binding.chipStatus.setOnCheckedStateChangeListener(onChipSelectedListener())

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
                    viewModel.getItemsServices(mutableMap[checkedIds[0]]!!, isProducts)
                } else {
                    binding.chipStatus.check(statusId)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mutableMap[binding.chipPending.id] = 1
        mutableMap[binding.chipAccepted.id] = 2
        mutableMap[binding.chipRejected.id] = 3
        mutableMap[binding.chipSold.id] = 4
        mutableMap[binding.chipCancel.id] = 5
        statusId = binding.chipPending.id
        binding.chipStatus.check(statusId)
        viewModel.getItemsServices(mutableMap[statusId]!!, isProducts)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData(statusId, isProducts)
        //binding.chipStatus.check(statusId)
        sharedViewModel.reloadClickDone()
    }

    override fun onClick(item: ProductStoreItemState?, itemId: Int, userAction: Int) {
        when (statusId) {
            binding.chipPending.id -> showBottomSheet(item,itemId, userAction, 1)
            binding.chipAccepted.id -> showBottomSheet(item,itemId, userAction, 2)
        }
    }

    private fun showBottomSheet(item: ProductStoreItemState?,itemId: Int, userAction: Int, status: Int) {
        val dialog = BottomSheetDialog(requireContext())
        val bindingBottomSheet = LayoutBottomSheetPendingBinding.inflate(layoutInflater)
        //bindingBottomSheet.
        dialog.setContentView(bindingBottomSheet.root)

        //dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        bindingBottomSheet.userAction = userAction
        bindingBottomSheet.state = status

        bindingBottomSheet.txtCancelPending.setOnClickListener {
            dialog.cancel()
        }

        bindingBottomSheet.txtRequestToDelete.setOnClickListener {
            viewModel.requestToDelete(itemId, isProducts)
        }
        bindingBottomSheet.txtProceedSale.setOnClickListener {  }
        bindingBottomSheet.txtRequestToEdit.setOnClickListener {
            sharedViewModel.navigateToSell(item)
        }
    }

}