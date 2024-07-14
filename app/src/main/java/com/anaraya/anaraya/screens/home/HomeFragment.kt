package com.anaraya.anaraya.screens.home

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.anaraya.anaraya.MainActivity
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentHomeBinding
import com.anaraya.anaraya.databinding.LayoutDialogSurveyImageBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.home.adapter.HomeAdsAdapter
import com.anaraya.anaraya.screens.home.adapter.HomeCategoryAdapter
import com.anaraya.anaraya.screens.home.adapter.HomePointsProductAdapter
import com.anaraya.anaraya.screens.home.adapter.HomeTrendingProductAdapter
import com.anaraya.anaraya.screens.home.interaction.HomeAdsInteraction
import com.anaraya.anaraya.screens.home.interaction.ProductPointsInteractionListener
import com.anaraya.anaraya.screens.shared_interaction.CategoryInteraction
import com.anaraya.anaraya.screens.shared_interaction.ProductInteractionListener
import com.anaraya.anaraya.util.plusNumBasket
import com.anaraya.anaraya.util.removeUser
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showButtonCart
import com.anaraya.anaraya.util.showButtonFav
import com.anaraya.anaraya.util.showButtonFilter
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showTextSchedule
import com.anaraya.anaraya.util.showToolBar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), ProductInteractionListener, CategoryInteraction,
    ProductPointsInteractionListener,
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
    private var posPoint = -1
    private var noInternet = false
    private var setTextTrending = false
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // sharedPreferences.edit().putBoolean("notification", true).apply()
            // sendFCMToken()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.you_will_not_received_notifications), Toast.LENGTH_SHORT
            ).show()
            // sharedPreferences.edit().putBoolean("notification", false).apply()
            // sendFCMToken()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
            sharedViewModel.homeState.collectLatest {
                if (it.navigateToFav) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
                    sharedViewModel.navigateToFavDone()
                }
                if (it.navigateToBrand) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                            selectionType = 4,
                            id = sharedViewModel.homeState.value.navigationId
                        )
                    )
                    sharedViewModel.navigateToBrandDone()
                }
                if (it.navigateToMainCat) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                            selectionType = 2,
                            id = sharedViewModel.homeState.value.navigationId
                        )
                    )
                    sharedViewModel.navigateToMainCatDone()
                }
                if (it.navigateToCat) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                            selectionType = 3,
                            id = sharedViewModel.homeState.value.navigationId
                        )
                    )
                    sharedViewModel.navigateToCatDone()
                }
                if (it.navigateToProduct) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
                            productId = sharedViewModel.homeState.value.navigationId.toInt(),
                            isPoints = false
                        )
                    )
                    sharedViewModel.navigateToProductDone()
                }
                if (it.navigateToSurvey) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSurveysFragment()
                    )
                    sharedViewModel.navigateToSurveyDone()
                }
                if (it.navigateToMarketPlaceProduct) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStoreFragment()
                    )
                    sharedViewModel.navigateToMarketPlaceProductDone()
                }
                if (it.navigateToMarketPlaceService) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStoreServiceFragment()
                    )
                    sharedViewModel.navigateToMarketPlaceServiceDone()
                }
                if(it.getCart) {
                    viewModel.getCartData()
                    sharedViewModel.getCartDone()
                }
                if(it.getTrendingData) {
                    viewModel.getHomeTrending()
                    sharedViewModel.getTrendingDone()
                }
                if(it.getPointsData) {
                    viewModel.getHomePoints()
                    sharedViewModel.getPointsDone()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.navigateToCart.collectLatest {
                if (it) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCartFragment())
                    sharedViewModel.navigateToCartDone()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeUiState.collectLatest {
                if (it.navigateToSchedule) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToScheduleFragment())
                    viewModel.navigateToScheduleDone()
                }
                if (it.navigateToSearch) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                            -1,
                            "-1"
                        )
                    )
                    viewModel.navigateToSearchDone()
                }
                if (it.navigateToAllProduct) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToAllProductFragment(
                            viewModel.trendingProducts.value.trendingText
                        )
                    )
                    viewModel.navigateToAllDone()
                }
                if (it.navigateToPointsProducts) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToPointsProductsFragment()
                    )
                    viewModel.navigateToPointsProductsDone()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.survey.collectLatest {
                if (!it.surveyImage.isNullOrBlank()) {
                    if (sharedViewModel.homeState.value.showSurveyImage) {
                        showDialogSurvey(it.surveyImage, it.surveyId)
                        sharedViewModel.showSurveyImage(false)
                    }
                }
            }
        }

        binding.txtViewAllTrendingHome.setOnClickListener {
            viewModel.navigateToAll()
        }
        binding.txtViewAllPointsHome.setOnClickListener {
            viewModel.navigateToPointsProducts()
        }
        binding.txtPointsHome.setOnClickListener {
            viewModel.navigateToPointsProducts()
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
        if (!sharedPreferences.getBoolean("notificationFlagIsSet", false))
            askNotificationPermission()
        val trendingAdapter = HomeTrendingProductAdapter(this)
        binding.recyclerTrendingProductHome.adapter = trendingAdapter
        val pointsAdapter = HomePointsProductAdapter(this)
        binding.recyclerPointsProductHome.adapter = pointsAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsAds.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
//                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.productsAdsUiState.isNotEmpty()) {
                    size = it.productsAdsUiState.size
                    initSlider(it.productsAdsUiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trendingProducts.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
//                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.trendingProductUiState != null) {
                    it.trendingProductUiState.collect { p ->
                        trendingAdapter.submitData(p)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pointsProducts.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
//                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.pointsProductUiState != null) {
                    it.pointsProductUiState.collect { p ->
                        pointsAdapter.submitData(p)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addToCart.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error == getString(R.string.no_internet)) {
                        if (!noInternet) {
                            noInternet = true
//                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.addCartUiState != null) {
                    if (!it.isSucceedAddCartUiState)
                        Toast.makeText(context, it.addCartUiState, Toast.LENGTH_SHORT).show()
                    else {
                        if (pos > -1) {
//                            trendingAdapter.changeIcon(pos)
                            pos = -1
                            plusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                        }
                    }
                }
                if (it.addPointCartUiState != null) {
                    if (!it.isSucceedAddPointCartUiState)
                        Toast.makeText(context, it.addPointCartUiState, Toast.LENGTH_SHORT)
                            .show()
                    else {
                        if (posPoint > -1) {
//                            pointsAdapter.changeIcon(posPoint)
                            posPoint = -1
                            plusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartUiState.collectLatest {
                if (it.isSucceedGetCartData) {
                    sharedPreferences.edit().putInt(
                        requireContext().getString(R.string.productsinbasket),
                        it.numOfBasket
                    ).apply()
                    sharedViewModel.setProductInBasket(it.numOfBasket)
                    sharedViewModel.getCartDone()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            trendingAdapter.loadStateFlow.collectLatest {
                if (it.append is LoadState.NotLoading) {
                    viewModel.visibilityTrending(trendingAdapter.snapshot().isNotEmpty())
                    if (trendingAdapter.snapshot().isNotEmpty())
                        if (!setTextTrending) {
                            viewModel.updateTextTrending(
                                trendingAdapter.snapshot().first()!!.textTrending!!
                            )
                            setTextTrending = true
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            pointsAdapter.loadStateFlow.collectLatest {
                if (it.append is LoadState.NotLoading)
                    viewModel.visibilityPoints(pointsAdapter.snapshot().isNotEmpty())
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
//                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    } else {
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
            trendingAdapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            pointsAdapter.loadStateFlow.collectLatest {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapterCategory.loadStateFlow.collectLatest {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }

        trendingAdapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                if (it.error.message == "Not Found") {
                    if (trendingAdapter.snapshot().isEmpty())
                        viewModel.setHomeTrendingError(it.error.message)
                } else {
                    if (it.error.message == "Connection reset") {
                        logOut()
                    }
                    viewModel.setHomeTrendingError(it.error.message)
                }
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
                if (it.error.message == "Not Found") {
                    if (pointsAdapter.snapshot().isEmpty())
                        viewModel.setHomePointsError(it.error.message)
                } else {
                    if (it.error.message == "Connection reset") {
                        logOut()
                    }
                    viewModel.setHomePointsError(it.error.message)
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
                categoryId = categoryId,
                categoryName = categoryName,
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

    override fun adClick(id: Int, type: Int, adLink: String?) {
        when (type) {
            1 -> findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
                    id
                )
            )

            2 -> findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    selectionType = type,
                    id = id.toString()
                )
            )

            3 -> findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    selectionType = type,
                    id = id.toString()
                )
            )

            4 -> findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    selectionType = type,
                    id = id.toString()
                )
            )

            5 -> {
                adLink?.let {
                    if (it.startsWith("http://") || it.startsWith("https://")) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(it)
                        startActivity(intent)
                    } else {
                        val s = StringBuilder()
                        s.insert(0, "https://$it")
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(s.toString())
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCLickPointProduct(productId: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
                productId = productId, isPoints = false
            )
        )
    }

    override fun addToCartPointProduct(productId: Int, position: Int) {
        posPoint = position
        viewModel.addToCartPoint(productId, 1)
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            }
//            else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//
//            }
            else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showDialogSurvey(surveyImage: String, surveyId: Int) {
        val view = LayoutDialogSurveyImageBinding.inflate(layoutInflater)
        view.image = surveyImage
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.setCancelable(false)
        dialog.setView(view.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.survey_dialog_shape)
        dialog.show()

        view.imgSurvey.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSurveyDetailsFragment(
                    surveyId = surveyId,
                    surveyTitle = ""
                )
            )
        }
        view.btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

}