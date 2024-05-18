package com.anaraya.anaraya.home.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.anaraya.anaraya.MainActivity
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentHomeBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.home.adapter.HomeAdsAdapter
import com.anaraya.anaraya.home.home.adapter.HomeCategoryAdapter
import com.anaraya.anaraya.home.home.adapter.HomeTrendingProductAdapter
import com.anaraya.anaraya.home.shared_interaction.CategoryInteraction
import com.anaraya.anaraya.home.shared_interaction.ProductInteractionListener
import com.anaraya.anaraya.util.plusNumBasket
import com.anaraya.anaraya.util.removeUser
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showButtonCart
import com.anaraya.anaraya.util.showButtonFav
import com.anaraya.anaraya.util.showButtonFilter
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showTextBadge
import com.anaraya.anaraya.util.showTextSchedule
import com.anaraya.anaraya.util.showToolBar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), ProductInteractionListener, CategoryInteraction,
    HomeAdsInteraction {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var bottomNav: ChipNavigationBar
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var edtSearch: EditText
    private lateinit var btnCart: ImageButton
    private lateinit var cardSearch: CardView
    private var size = 0
    private var plus = true
    private var pos = -1
    private var noInternet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = viewModel

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        edtSearch = requireActivity().findViewById(R.id.edtSearch)
        btnCart = requireActivity().findViewById(R.id.btnCart)
        cardSearch = requireActivity().findViewById(R.id.cardSearch_homeActivity)

        edtSearch.clearFocus()
        viewModel.navigateToSearchDone()

        val textSchedule = requireActivity().findViewById<TextView>(R.id.txtDelivery_homeActivity)
        textSchedule.setOnClickListener {
            viewModel.navigateToSchedule()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeUiState.collectLatest {
                if (it.navigateToSchedule) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToScheduleFragment())
                    viewModel.navigateToScheduleDone()
                }
                if (it.navigateToSearch) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                    viewModel.navigateToSearchDone()
                }
                if (it.navigateToAllProduct) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAllProductFragment())
                    viewModel.navigateToAllDone()
                }
            }
        }

        lifecycleScope.launch {
            sharedViewModel.navigateToCart.collectLatest {
                if (it) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCartFragment())
                    sharedViewModel.navigateToCartDone()
                }
            }
        }
        lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.navigateToFav) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
                    sharedViewModel.navigateToFavDone()
                }
            }
        }


        binding.txtViewAllTrendingHome.setOnClickListener {
            viewModel.navigateToAll()
        }

        btnCart.setOnClickListener {
            sharedViewModel.navigateToCart()
        }

        edtSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.navigateToSearch()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeTrendingProductAdapter(this)
        binding.recyclerTrendingProductHome.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsAds.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.productsAdsUiState.isNotEmpty()) {
                    size = it.productsAdsUiState.size
                    initSlider(it.productsAdsUiState)
                }
                if (it.addCartUiState != null) {
                    if (pos > -1) {
                        //Toast.makeText(context, it.addCartUiState, Toast.LENGTH_SHORT).show()
                        adapter.changeIcon(pos)
                        pos = -1
                        plusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
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
        binding.recyclerCategoriesHome.adapter = adapterCategory

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
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
                    if (it.error.message == "Connection reset") {
                        logOut()
                    }
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

    private fun initSlider(productAdUiState: List<ProductAdUiState>) {
        val adsAdapter = HomeAdsAdapter(this)
        binding.viewPagerSliderHome.adapter = adsAdapter
        adsAdapter.submitList(productAdUiState)
        binding.dotsIndicatorSliderHome.attachTo(binding.viewPagerSliderHome)
        binding.viewPagerSliderHome.postDelayed(object : Runnable {
            override fun run() {
                binding.viewPagerSliderHome.currentItem =
                    (binding.viewPagerSliderHome.currentItem + 1) % size
                binding.viewPagerSliderHome.postDelayed(this, (size * 2000).toLong())
            }
        }, 2000)

    }

    override fun onStart() {
        super.onStart()
        bottomNav = requireActivity().findViewById(R.id.bottomNavHome)
        showToolBar(requireActivity(), false)
        showCardHome(requireActivity(), true)
        showBottomNavBar(requireActivity(), true)
        showTextSchedule(requireActivity(), true)
        showButtonCart(requireActivity(), true)
        showButtonFilter(requireActivity(), false)
        showButtonFav(requireActivity(), true)
        showTextBadge(requireActivity(), true, sharedPreferences, requireContext())
    }

    override fun onCLick(productId: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
                productId
            )
        )
    }

    override fun addToCart(productId: Int, position: Int) {
        pos = position
        viewModel.addToCart(productId, 1)
    }

    override fun onClickItem(categoryId: Int, categoryName: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToCategoryProductFragment(
                categoryName,
                categoryId
            )
        )
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    private fun logOut() {
        removeUser(sharedPreferences, requireContext())
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        //edtSearch.isEnabled = false
    }

    override fun adClick(productId: Int, isProduct: Boolean) {
        if (isProduct)
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
                    productId
                )
            )

    }

//    override fun onResume() {
//        super.onResume()
//        edtSearch.clearFocus()
//    }

}