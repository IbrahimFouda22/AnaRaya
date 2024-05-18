package com.anaraya.anaraya.home_design

import android.content.SharedPreferences
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
import com.anaraya.anaraya.MainActivityViewModel
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentHomeDesignBinding
import com.anaraya.anaraya.home.home.HomeAdsInteraction
import com.anaraya.anaraya.home.home.ProductAdUiState
import com.anaraya.anaraya.home.home.adapter.HomeAdsAdapter
import com.anaraya.anaraya.home.home.adapter.HomeCategoryAdapter
import com.anaraya.anaraya.home.home.adapter.HomeTrendingProductAdapter
import com.anaraya.anaraya.home.shared_interaction.CategoryInteraction
import com.anaraya.anaraya.home.shared_interaction.ProductInteractionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeDesignFragment : Fragment() , ProductInteractionListener, CategoryInteraction,
    HomeAdsInteraction {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentHomeDesignBinding
    private var size = 0
    private val viewModel by viewModels<HomeDesignViewModel>({ this })
    private val sharedViewModel by viewModels<MainActivityViewModel>({ requireActivity() })

    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeDesignBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        btnBack = requireActivity().findViewById(R.id.btnBackMainActivity)
        btnReload = requireActivity().findViewById(R.id.btnReloadMain)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeUiState.collectLatest {
                if(it.navigateToAuth){
                    findNavController().navigate(HomeDesignFragmentDirections.actionHomeDesignFragmentToAuthenticationFragment())
                    viewModel.navigateToAuthDone()
                }
            }
        }

        binding.bottomNavHomeDesign.setItemSelected(R.id.homeFragment)
        binding.bottomNavHomeDesign.setOnItemSelectedListener {
            navigatePage(it)
        }

        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeTrendingProductAdapter(this)
        binding.recyclerTrendingProductHomeDesign.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsAds.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.productsAdsUiState.isNotEmpty()) {
                    size = it.productsAdsUiState.size
                    initSlider(it.productsAdsUiState)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.productUiState != null) {
                    it.productUiState.collect { p ->
                        adapter.submitData(p)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                if (it.append is LoadState.NotLoading)
                    viewModel.visibilityTrending(adapter.snapshot().isNotEmpty())
            }
        }

        val adapterCategory = HomeCategoryAdapter(this)
        binding.recyclerCategoriesHomeDesign.adapter = adapterCategory

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.categoryUiState != null) {
                    it.categoryUiState.collect { p ->
                        adapterCategory.submitData(p)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapterCategory.loadStateFlow.collectLatest {
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
                if (it.error.message == "Not Found") {
                    if (adapter.snapshot().isEmpty())
                        viewModel.setHomeTrendingError(it.error.message)
                } else {
                    viewModel.setHomeTrendingError(it.error.message)
                }
            }
        }
        adapterCategory.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                if (it.error.message == "Not Found") {
                    if (adapterCategory.snapshot().isEmpty())
                        viewModel.setHomeMainCatError(it.error.message)
                } else {
                    viewModel.setHomeMainCatError(it.error.message)
                }
            }
        }
    }

    private fun navigatePage(id: Int) {
        when (id) {
            R.id.homeFragment -> viewModel.navigateToAuth()
            R.id.servicesFragment -> viewModel.navigateToAuth()
            R.id.moreFragment -> viewModel.navigateToAuth()
        }
    }

    override fun onClickItem(categoryId: Int, categoryName: String) {
        viewModel.navigateToAuth()
    }

    override fun onCLick(productId: Int) {
        viewModel.navigateToAuth()
    }

    override fun addToCart(productId: Int, position: Int) {
        viewModel.navigateToAuth()
    }

    private fun initSlider(productAdUiState: List<ProductAdUiState>) {
        val adsAdapter = HomeAdsAdapter(this)
        binding.viewPagerSliderHomeDesign.adapter = adsAdapter
        adsAdapter.submitList(productAdUiState)
        binding.dotsIndicatorSliderHomeDesign.attachTo(binding.viewPagerSliderHomeDesign)
        binding.viewPagerSliderHomeDesign.postDelayed(object : Runnable {
            override fun run() {
                binding.viewPagerSliderHomeDesign.currentItem =
                    (binding.viewPagerSliderHomeDesign.currentItem + 1) % size
                binding.viewPagerSliderHomeDesign.postDelayed(this, (size * 2000).toLong())
            }
        }, 2000)

    }
    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun adClick(productId: Int, isProduct: Boolean) {
        viewModel.navigateToAuth()
    }
}