package com.anaraya.anaraya.screens.points_products

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
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentPointsProductsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.home.interaction.ProductPointsInteractionListener
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PointsProductsFragment : Fragment(), ProductPointsInteractionListener {
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<PointsProductsViewModel>({ this })
    private lateinit var binding: FragmentPointsProductsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPointsProductsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val pointsAdapter = PointsProductsAdapter(this)
        binding.recyclerPointsProducts.adapter = pointsAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.addCartUiState != null) {
                    if (!it.isSucceedAddCartUiState)
                        Toast.makeText(context, it.addCartUiState, Toast.LENGTH_SHORT).show()
                    else {
                        sharedViewModel.getCart()
//                        sharedViewModel.getPoints()
                    }
                }
                if (it.products != null) {
                    it.products.collect { data ->
                        pointsAdapter.submitData(data)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pointsAdapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.source.refresh is LoadState.Loading)
            }
        }

        pointsAdapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                viewModel.setError(it.error.message)
            }
        }

        binding.btnBackPointsProducts.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSearchPointsProduct.setOnClickListener {
            findNavController().navigate(PointsProductsFragmentDirections.actionPointsProductsFragmentToSearchFragment())
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

    override fun onCLickPointProduct(productId: Int) {
        findNavController().navigate(
            PointsProductsFragmentDirections.actionPointsProductsFragmentToProductDetailsFragment(
                productId = productId, isPoints = true
            )
        )
    }

    override fun addToCartPointProduct(productId: Int, position: Int) {
        viewModel.addToCart(productId, 1)
    }

}