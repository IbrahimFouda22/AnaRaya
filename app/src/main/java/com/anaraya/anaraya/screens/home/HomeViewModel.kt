package com.anaraya.anaraya.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.screens.category_product.toUiState
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Cart
import com.anaraya.domain.entity.MainCategory
import com.anaraya.domain.entity.Product
import com.anaraya.domain.entity.ProductAd
import com.anaraya.domain.entity.SurveyImage
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageCategoryUseCase
import com.anaraya.domain.usecase.ManageProductUseCase
import com.anaraya.domain.usecase.ManageProfileUseCase
import com.anaraya.domain.usecase.ManageSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val manageCartUseCase: ManageCartUseCase,
    private val manageSurveysUseCase: ManageSurveysUseCase,
    private val manageProfileUseCase: ManageProfileUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState as StateFlow<HomeUiState>

    private val _productsAds = MutableStateFlow(HomeUiState())
    val productsAds = _productsAds as StateFlow<HomeUiState>

    private val _addToCart = MutableStateFlow(HomeUiState())
    val addToCart = _addToCart as StateFlow<HomeUiState>

    private val _cartUiState = MutableStateFlow(HomeUiState())
    val cartUiState = _cartUiState as StateFlow<HomeUiState>

    private val _trendingProducts = MutableStateFlow(HomeUiState())
    val trendingProducts = _trendingProducts as StateFlow<HomeUiState>

    private val _pointsProducts = MutableStateFlow(HomeUiState())
    val pointsProducts = _pointsProducts as StateFlow<HomeUiState>

    private val _categories = MutableStateFlow(HomeUiState())
    val categories = _categories as StateFlow<HomeUiState>

    private val _survey = MutableStateFlow(HomeUiState())
    val survey = _survey as StateFlow<HomeUiState>

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    init {
        getAllData()
    }

    fun getAllData() {
        getProductsAds()
        getTrendingProducts()
        getHomeCategory()
        //getPointsProducts()
        getCartData()
        getSurveyImage()
        getLoyaltyPoints()
    }

    fun getHomeTrending() {
        getTrendingProducts()
    }

    fun getHomePoints() {
        getLoyaltyPoints()
        getPointsProducts()
    }
    private fun getProductsAds() {
        //showLoading(true)
        _productsAds.update {
            HomeUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
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
                        adsType = it.adsType,
                        adsLink = it.adsLink
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

    private fun getTrendingProducts() {
        showLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            _trendingProducts.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onGetTrendingProductsSuccess(
                    manageProductUseCase.getTrendingProducts().cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetTrendingProductsFailure("No Internet")
            } catch (e: Exception) {
                onGetTrendingProductsFailure(e.message.toString())
            }
        }
    }

    private fun onGetTrendingProductsSuccess(list: Flow<PagingData<Product>>) {
        showLoading(false)
        _trendingProducts.update {
            it.copy(
                isLoading = false,
                error = null,
                trendingProductUiState = list.map { data ->
                    data.map { p ->
                        p.toUiState()
                    }
                },
            )
        }
    }

    private fun onGetTrendingProductsFailure(error: String) {
        showLoading(false)
        _trendingProducts.update {
            HomeUiState(
                isLoading = false,
                error = error,
                trendingProductUiState = null
            )
        }
    }

    fun updateTextTrending(text: String){
        _trendingProducts.update {
            it.copy(
                trendingText = text
            )
        }
    }

    private fun getPointsProducts() {
        showLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            _pointsProducts.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onGetPointsProductsAdsSuccess(
                    manageProductUseCase.getPointsProducts().cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetPointsProductsAdsFailure("No Internet")
            } catch (e: Exception) {
                onGetPointsProductsAdsFailure(e.message.toString())
            }
        }
    }

    private fun onGetPointsProductsAdsSuccess(list: Flow<PagingData<Product>>) {
        showLoading(false)
        _pointsProducts.update {
            it.copy(
                isLoading = false,
                error = null,
                pointsProductUiState = list.map { data ->
                    data.map { p ->
                        p.toUiState(isPoints = true)
                    }
                }
            )
        }
    }

    private fun onGetPointsProductsAdsFailure(error: String) {
        showLoading(false)
        _pointsProducts.update {
            it.copy(
                isLoading = false,
                error = error,
                pointsProductUiState = null
            )
        }
    }

    private fun getHomeCategory() {
        showLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
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
        _addToCart.update {
            it.copy(
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
        _addToCart.update {
            it.copy(
                isLoading = false,
                error = null,
                addCartUiState = response.message,
                isSucceedAddCartUiState = response.isSucceed
            )
        }
        if (response.isSucceed)
            getTrendingProducts()
    }

    private fun onAddToCartFailure(error: String) {
        showLoading(false)
        _addToCart.update {
            it.copy(
                isLoading = false,
                error = error,
                addCartUiState = null,
                isSucceedAddCartUiState = false
            )
        }
    }

    fun addToCartPoint(productId: Int, productQty: Int) {
        showLoading(true)
        _addToCart.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onAddToCartPointSuccess(manageCartUseCase.addPointCart(productId, productQty))
            } catch (e: NoInternetException) {
                onAddToCartPointFailure("No Internet")
            } catch (e: Exception) {
                onAddToCartPointFailure(e.message.toString())
            }
        }
    }

    private fun onAddToCartPointSuccess(response: AddRemoveCart) {
        showLoading(false)
        _addToCart.update {
            it.copy(
                isLoading = false,
                error = null,
                addPointCartUiState = response.message,
                isSucceedAddPointCartUiState = response.isSucceed
            )
        }
        if(response.isSucceed)
            getHomePoints()
    }

    private fun onAddToCartPointFailure(error: String) {
        showLoading(false)
        _addToCart.update {
            it.copy(
                isLoading = false,
                error = error,
                addPointCartUiState = null,
                isSucceedAddPointCartUiState = false
            )
        }
    }

    fun getCartData() {
        _cartUiState.update {
            HomeUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetProductsSuccess(manageCartUseCase.getCart())
            } catch (e: NoInternetException) {
                onGetProductsFailure("No Internet")
            } catch (e: Exception) {
                onGetProductsFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductsSuccess(cart: Cart) {
        if (cart.data.data.isEmpty() && cart.data.loyaltyData.isEmpty()) {
            _cartUiState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    isSucceedGetCartData = true,
                    numOfBasket = 0
                )
            }
        } else {
            _cartUiState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    isSucceedGetCartData = true,
                    numOfBasket = cart.data.data.size + cart.data.loyaltyData.size
                )
            }
        }
    }

    private fun onGetProductsFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceedGetCartData = false,
                numOfBasket = 0
            )
        }
    }

    private fun getSurveyImage() {
        _survey.update {
            HomeUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetSurveyImageSuccess(manageSurveysUseCase.getSurveyImage())
            } catch (e: NoInternetException) {
                onGetSurveyImageFailure("No Internet")
            } catch (e: Exception) {
                onGetSurveyImageFailure(e.message.toString())
            }
        }
    }

    private fun onGetSurveyImageSuccess(response: SurveyImage) {
        // showLoading(false)
        _survey.update {
            HomeUiState(
                isLoading = false,
                error = null,
                surveyImage = response.data.imageUrl,
                surveyId = response.data.surveyId
            )
        }
    }

    private fun onGetSurveyImageFailure(error: String) {
        //showLoading(false)
        _productsAds.update {
            HomeUiState(
                isLoading = false,
                error = error,
                productsAdsUiState = arrayListOf()
            )
        }
    }
    private fun getLoyaltyPoints() {
        _pointsProducts.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetLoyaltyPointsSuccess(manageProfileUseCase.getUserLoyaltyPoints())
            } catch (e: NoInternetException) {
                onGetLoyaltyPointsFailure("No Internet")
            } catch (e: Exception) {
                onGetLoyaltyPointsFailure(e.message.toString())
            }
        }
    }

    private fun onGetLoyaltyPointsSuccess(response: BaseResponse) {
        _pointsProducts.update {
            it.copy(
                isLoading = false, error = null, loyaltyPoints = response.data?.toInt() ?: 0
            )
        }
    }

    private fun onGetLoyaltyPointsFailure(error: String) {
        _pointsProducts.update {
            it.copy(
                isLoading = false, error = error, loyaltyPoints = 0
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
    fun navigateToPointsProducts() {
        _homeUiState.update {
            HomeUiState(
                navigateToPointsProducts = true
            )
        }
    }

    fun navigateToPointsProductsDone() {
        _homeUiState.update {
            HomeUiState(
                navigateToPointsProducts = false
            )
        }
    }

    fun setHomeTrendingError(error: String?) {
        _trendingProducts.update {
            HomeUiState(
                isLoading = false, error = error, trendingProductUiState = null
            )
        }
    }

    fun setHomePointsError(error: String?) {
        _trendingProducts.update {
            HomeUiState(
                isLoading = false, error = error, pointsProductUiState = null
            )
        }
    }

    fun setHomeMainCatError(error: String?) {
        _categories.update {
            HomeUiState(
                isLoading = false, error = error, categoryUiState = null
            )
        }
    }

    fun visibilityTrending(boolean: Boolean) {
        _trendingProducts.update {
            it.copy(
                visibilityTrending = boolean
            )
        }
    }

    fun visibilityPoints(boolean: Boolean) {
        _pointsProducts.update {
            it.copy(
                visibilityPoints = boolean
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }

}