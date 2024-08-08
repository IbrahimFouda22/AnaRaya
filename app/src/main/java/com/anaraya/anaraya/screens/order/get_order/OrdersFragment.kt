package com.anaraya.anaraya.screens.order.get_order

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
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentOrdersBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.order.get_order.adapter.OrdersAdapter
import com.anaraya.anaraya.screens.order.get_order.interaction.OrderInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment(), OrderInteraction {

    private lateinit var binding: FragmentOrdersBinding
    private val viewModel by viewModels<OrdersViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(layoutInflater)

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapter = OrdersAdapter(this)
        binding.recyclerOrder.adapter = adapter

        lifecycleScope.launch {
            viewModel.orderUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if(it.ordersUiState.isNotEmpty()){
                    adapter.submitList(it.ordersUiState)
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

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(),false)
        showToolBar(requireActivity(),true)
    }

    override fun onClickDelete(orderId: Int) {
        viewModel.deleteOrder(orderId)
    }

}