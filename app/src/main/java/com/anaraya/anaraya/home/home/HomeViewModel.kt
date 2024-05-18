package com.anaraya.anaraya.home.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.home.category_product.toUiState
import com.anaraya.anaraya.home.shared_data.ProductUiState
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.MainCategory
import com.anaraya.domain.entity.Product
import com.anaraya.domain.entity.ProductAd
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageCategoryUseCase
import com.anaraya.domain.usecase.ManageProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val manageProductUseCase: ManageProductUseCase,
    private val manageCategoryUseCase: ManageCategoryUseCase,
    private val manageCartUseCase: ManageCartUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState as StateFlow<HomeUiState>

    private val _productsAds = MutableStateFlow(HomeUiState())
    val productsAds = _productsAds as StateFlow<HomeUiState>

    private val _products = MutableStateFlow(HomeUiState())
    val products = _products as StateFlow<HomeUiState>

    private val _categories = MutableStateFlow(HomeUiState())
    val categories = _categories as StateFlow<HomeUiState>


    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>
//
//    private val _navigateToSchedule = MutableStateFlow(false)
//    val navigateToSchedule = _navigateToSchedule as StateFlow<Boolean>
//
//    private val _navigateToSearch = MutableStateFlow(false)
//    val navigateToSearch = _navigateToSearch as StateFlow<Boolean>
//
//    private val _navigateToAllProduct = MutableStateFlow(false)
//    val navigateToAllProduct = _navigateToAllProduct as StateFlow<Boolean>


    init {
        getProductsAds()
        getTrendingProductsAds()
        getHomeCategory()
    }

    fun getAllData() {
        getProductsAds()
        getTrendingProductsAds()
        getHomeCategory()
    }

    private fun getProductsAds() {
        //showLoading(true)
        _productsAds.update {
            HomeUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetProductsAdsSuccess(manageProductUseCase.getProductsAds())
            } catch (e: NoInternetException) {
                onGetProductsAdsFailure("No Internet")
            } catch (e: Exception) {
                onGetProductsAdsFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductsAdsSuccess(list: List<ProductAd>) {
       // showLoading(false)
        _productsAds.update {
            HomeUiState(
                isLoading = false,
                error = null,
                productsAdsUiState = list.map {
                    ProductAdUiState(
                        id = it.id,
                        image = it.image,
                        isProductOrBrand = it.isProductOrBrand
                    )
                }
            )
        }
    }

    private fun onGetProductsAdsFailure(error: String) {
        //showLoading(false)
        _productsAds.update {
            HomeUiState(
                isLoading = false,
                error = error,
                productsAdsUiState = arrayListOf()
            )
        }
    }

    private fun getTrendingProductsAds() {
        showLoading(true)
        viewModelScope.launch {
            _products.update {
                HomeUiState(
                    isLoading = true,
                    error = null,
                    productUiState = null
                )
            }
            try {
                onGetTrendingProductsAdsSuccess(
                    manageProductUseCase.getTrendingProducts().cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetProductsAdsFailure("No Internet")
            } catch (e: Exception) {
                onGetTrendingProductsAdsFailure(e.message.toString())
            }
        }
    }

    private fun onGetTrendingProductsAdsSuccess(list: Flow<PagingData<Product>>) {
        showLoading(false)
        _products.update {
            HomeUiState(
                isLoading = false,
                error = null,
                productUiState = list.map {
                    it.map { p ->
                        p.toUiState()
                    }
                }
            )
        }
    }

    private fun onGetTrendingProductsAdsFailure(error: String) {
        showLoading(false)
        _products.update {
            HomeUiState(
                isLoading = false,
                error = error,
                productsAdsUiState = arrayListOf()
            )
        }
    }

    private fun getHomeCategory() {
        showLoading(true)
        viewModelScope.launch {
            _categories.update {
                HomeUiState(
                    isLoading = true,
                    error = null
                )
            }
            try {
                onGetHomeCategorySuccess(
                    manageCategoryUseCase.getHomeCategory().cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetProductsAdsFailure("No Internet")
            } catch (e: Exception) {
                onGetHomeCategoryFailure(e.message.toString())
            }
        }
    }

    private fun onGetHomeCategorySuccess(list: Flow<PagingData<MainCategory>>) {
        showLoading(false)
        _categories.update {
            HomeUiState(
                isLoading = false,
                error = null,
                categoryUiState = list.map {
                    it.map { m ->
                        CategoryUiState(
                            image = m.image,
                            name = m.name,
                            id = m.id
                        )
                    }
                }
            )
        }
    }

    private fun onGetHomeCategoryFailure(error: String) {
        showLoading(false)
        _categories.update {
            HomeUiState(
                isLoading = false,
                error = error,
                categoryUiState = null
            )
        }
    }

    fun addToCart(productId: Int, productQty: Int) {
        showLoading(true)
        _productsAds.update {
            HomeUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onAddToCartSuccess(manageCartUseCase.addCart(productId, productQty))
            } catch (e: NoInternetException) {
                onAddToCartFailure("No Internet")
            } catch (e: Exception) {
                onAddToCartFailure(e.message.toString())
            }
        }
    }

    private fun onAddToCartSuccess(response: AddRemoveCart) {
        showLoading(false)
        _productsAds.update {
            it.copy(
                isLoading = false,
                error = null,
                addCartUiState = response.message
            )
        }
    }

    private fun onAddToCartFailure(error: String) {
        showLoading(false)
        _productsAds.update {
            it.copy(
                isLoading = false,
                error = error,
                addCartUiState = null
            )
        }
    }


    fun navigateToSchedule() {
        _homeUiState.update {
            HomeUiState(
                navigateToSchedule = true
            )
        }
    }

    fun navigateToScheduleDone() {
        _homeUiState.update {
            HomeUiState(
                navigateToSchedule = false
            )
        }
    }

    fun navigateToSearch() {
        _homeUiState.update {
            HomeUiState(
                navigateToSearch = true
            )
        }
    }

    fun navigateToSearchDone() {
        _homeUiState.update {
            HomeUiState(
                navigateToSearch = false
            )
        }
    }

    fun navigateToAll() {
        _homeUiState.update {
            HomeUiState(
                navigateToAllProduct = true
            )
        }
    }

    fun navigateToAllDone() {
        _homeUiState.update {
            HomeUiState(
                navigateToAllProduct = false
            )
        }
    }

    fun setHomeTrendingError(error: String?){
        _products.update {
            HomeUiState(
                isLoading = false, error = error, productUiState = null
            )
        }
    }

    fun setHomeMainCatError(error: String?){
        _categories.update {
            HomeUiState(
                isLoading = false, error = error, categoryUiState = null
            )
        }
    }

    fun visibilityTrending(boolean: Boolean){
        _products.update {
            it.copy(
                visibilityTrending = boolean
            )
        }
    }

    fun showLoading(boolean: Boolean){
        _loadingState.update {
            boolean
        }
    }

}