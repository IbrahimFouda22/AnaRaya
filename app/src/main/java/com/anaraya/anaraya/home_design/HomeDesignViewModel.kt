package com.anaraya.anaraya.home_design

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.home.category_product.toUiState
import com.anaraya.anaraya.home.home.CategoryUiState
import com.anaraya.anaraya.home.home.ProductAdUiState
import com.anaraya.domain.entity.MainCategory
import com.anaraya.domain.entity.Product
import com.anaraya.domain.entity.ProductAd
import com.anaraya.domain.exception.NoInternetException
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
class HomeDesignViewModel @Inject constructor(
    private val manageProductUseCase: ManageProductUseCase,
    private val manageCategoryUseCase: ManageCategoryUseCase,
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeDesignUiState())
    val homeUiState = _homeUiState as StateFlow<HomeDesignUiState>

    private val _productsAds = MutableStateFlow(HomeDesignUiState())
    val productsAds = _productsAds as StateFlow<HomeDesignUiState>

    private val _products = MutableStateFlow(HomeDesignUiState())
    val products = _products as StateFlow<HomeDesignUiState>

    private val _categories = MutableStateFlow(HomeDesignUiState())
    val categories = _categories as StateFlow<HomeDesignUiState>


    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>


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
            HomeDesignUiState(
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
            HomeDesignUiState(
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
            HomeDesignUiState(
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
                HomeDesignUiState(
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
            HomeDesignUiState(
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
            HomeDesignUiState(
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
                HomeDesignUiState(
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
            HomeDesignUiState(
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
            HomeDesignUiState(
                isLoading = false,
                error = error,
                categoryUiState = null
            )
        }
    }


    fun navigateToAuth() {
        _homeUiState.update {
            HomeDesignUiState(
                navigateToAuth = true
            )
        }
    }

    fun navigateToAuthDone() {
        _homeUiState.update {
            HomeDesignUiState(
                navigateToAuth = false
            )
        }
    }

    fun setHomeTrendingError(error: String?){
        _products.update {
            HomeDesignUiState(
                isLoading = false, error = error, productUiState = null
            )
        }
    }

    fun setHomeMainCatError(error: String?){
        _categories.update {
            HomeDesignUiState(
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