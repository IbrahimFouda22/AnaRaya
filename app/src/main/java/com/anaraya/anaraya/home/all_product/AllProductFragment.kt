package com.anaraya.anaraya.home.all_product

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
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentAllProductBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.all_product.adapter.AllProductAdapter
import com.anaraya.anaraya.home.shared_interaction.ProductInteractionListener
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllProductFragment : Fragment(), ProductInteractionListener {

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<AllProductViewModel>({ this })
    private lateinit var binding: FragmentAllProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllProductBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapter = AllProductAdapter(this)
        binding.recyclerAllProduct.adapter = adapter

        lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.addCartUiState != null) {
                    Toast.makeText(context, it.addCartUiState, Toast.LENGTH_SHORT).show()
                }
                if (it.products != null) {
                    it.products.collect { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.source.refresh is LoadState.Loading)
            }
        }

        adapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                viewModel.setError(it.error.message)
            }
        }

        binding.btnBackAllProduct.setOnClickListener {
            findNavController().popBackStack()
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
        showCardHome(requireActivity(), false)
        showBottomNavBar(requireActivity(), false)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onCLick(productId: Int) {
        findNavController().navigate(
            AllProductFragmentDirections.actionAllProductFragmentToProductDetailsFragment(
                productId
            )
        )
    }

    override fun addToCart(productId: Int, position: Int) {
        viewModel.addToCart(productId, 1)
    }

}