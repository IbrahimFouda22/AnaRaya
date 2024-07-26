package com.anaraya.anaraya.screens.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.screens.cart.CartUiList
import com.anaraya.anaraya.screens.services.store.product.my_items.ProductStoreItemState
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//    sharedPreferences: SharedPreferences,
@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeActivityUiState())
    val homeState = _homeState as StateFlow<HomeActivityUiState>
    private val _navigateToCart = MutableStateFlow(false)
    val navigateToCart = _navigateToCart as StateFlow<Boolean>

//    init {
//        setProductInBasket(
//            sharedPreferences.getInt(
//                context.getString(R.string.productsinbasket),
//                0
//            )
//        )
//    }

    fun setError(error: String?) {
        _homeState.update {
            HomeActivityUiState(
                error = error
            )
        }
    }

    fun setProductInBasket(num: Int) {
        _homeState.update {
            it.copy(
                productInBasket = num
            )
        }
    }

    fun getUserMyInfo() {
        _homeState.update {
            it.copy(
                getUserMyInfo = true
            )
        }
    }

    fun getUserMyInfoDone() {
        _homeState.update {
            it.copy(
                getUserMyInfo = false
            )
        }
    }

    fun getCart() {
        _homeState.update {
            it.copy(
                getCart = true
            )
        }
    }

    fun getCartDone() {
        _homeState.update {
            it.copy(
                getCart = false
            )
        }
    }

    fun getTrending() {
        _homeState.update {
            it.copy(
                getTrendingData = true
            )
        }
    }

    fun getTrendingDone() {
        _homeState.update {
            it.copy(
                getTrendingData = false
            )
        }
    }

    fun getPoints() {
        _homeState.update {
            it.copy(
                getPointsData = true
            )
        }
    }

    fun getPointsDone() {
        _homeState.update {
            it.copy(
                getPointsData = false
            )
        }
    }

    fun getAddresses() {
        _homeState.update {
            it.copy(
                getAddresses = true
            )
        }
    }

    fun getAddressesDone() {
        _homeState.update {
            it.copy(
                getAddresses = false
            )
        }
    }

    fun getUserMoreInfo() {
        _homeState.update {
            it.copy(
                getUserMoreInfo = true
            )
        }
    }

    fun getUserMoreDone() {
        _homeState.update {
            it.copy(
                getUserMoreInfo = false
            )
        }
    }

    fun reloadClick() {
        _homeState.update {
            HomeActivityUiState(
                error = null,
                reloadClick = true
            )
        }
    }

    fun reloadClickDone() {
        _homeState.update {
            HomeActivityUiState(
                error = null,
                reloadClick = false
            )
        }
    }

    fun navigateToCart() {
        _navigateToCart.update {
            true
        }
    }

    fun navigateToCartDone() {
        _navigateToCart.update {
            false
        }
    }

    fun navigateToFav() {
        _homeState.update {
            it.copy(navigateToFav = true)
        }
    }

    fun navigateToFavDone() {
        _homeState.update {
            it.copy(navigateToFav = false)
        }
    }


    fun navigateToItemDetailsProduct(productId: Int) {
        _homeState.update {
            it.copy(navigateToItemDetailsProduct = true, itemDetailsProductId = productId)
        }
    }

    fun navigateToItemDetailsProductDone() {
        _homeState.update {
            it.copy(navigateToItemDetailsProduct = false)
        }
    }
    fun navigateToItemSoldProduct(productId: Int) {
        _homeState.update {
            it.copy(navigateToItemSoldProduct = true, itemSoldProductId = productId)
        }
    }

    fun navigateToItemSoldProductDone() {
        _homeState.update {
            it.copy(navigateToItemSoldProduct = false)
        }
    }

    fun navigateToItemDetailsService(serviceId: Int) {
        _homeState.update {
            it.copy(navigateToItemDetailsService = true, itemDetailsServiceId = serviceId)
        }
    }

    fun navigateToItemDetailsServiceDone() {
        _homeState.update {
            it.copy(navigateToItemDetailsService = false)
        }
    }
    fun navigateToItemSoldService(serviceId: Int) {
        _homeState.update {
            it.copy(navigateToItemSoldService = true, itemSoldServiceId = serviceId)
        }
    }

    fun navigateToItemSoldServiceDone() {
        _homeState.update {
            it.copy(navigateToItemSoldService = false)
        }
    }

    fun navigateToBrand(id: String) {
        _homeState.update {
            it.copy(navigateToBrand = true, navigationId = id)
        }
    }

    fun navigateToBrandDone() {
        _homeState.update {
            it.copy(navigateToBrand = false)
        }
    }

    fun navigateToMainCat(id: String) {
        _homeState.update {
            it.copy(navigateToMainCat = true, navigationId = id)
        }
    }

    fun navigateToMainCatDone() {
        _homeState.update {
            it.copy(navigateToMainCat = false)
        }
    }

    fun navigateToCat(id: String) {
        _homeState.update {
            it.copy(navigateToCat = true, navigationId = id)
        }
    }

    fun navigateToCatDone() {
        _homeState.update {
            it.copy(navigateToCat = false)
        }
    }

    fun navigateToProduct(id: String) {
        _homeState.update {
            it.copy(navigateToProduct = true, navigationId = id)
        }
    }

    fun navigateToProductDone() {
        _homeState.update {
            it.copy(navigateToProduct = false)
        }
    }

    fun navigateToSurvey() {
        _homeState.update {
            it.copy(navigateToSurvey = true)
        }
    }

    fun navigateToSurveyDone() {
        _homeState.update {
            it.copy(navigateToSurvey = false)
        }
    }

    fun navigateToMarketPlaceProduct() {
        _homeState.update {
            it.copy(navigateToMarketPlaceProduct = true)
        }
    }

    fun navigateToMarketPlaceProductDone() {
        _homeState.update {
            it.copy(navigateToMarketPlaceProduct = false)
        }
    }
    fun navigateToMarketPlaceOwnerProduct(id: String) {
        _homeState.update {
            it.copy(navigateToMarketPlaceOwnerProduct = true, navigationId = id)
        }
    }

    fun navigateToMarketPlaceOwnerProductDone() {
        _homeState.update {
            it.copy(navigateToMarketPlaceOwnerProduct = false)
        }
    }

    fun navigateToMarketPlaceService() {
        _homeState.update {
            it.copy(navigateToMarketPlaceService = true)
        }
    }

    fun navigateToMarketPlaceServiceDone() {
        _homeState.update {
            it.copy(navigateToMarketPlaceService = false)
        }
    }
    fun navigateToMarketPlaceOwnerService(id: String) {
        _homeState.update {
            it.copy(navigateToMarketPlaceOwnerService = true, navigationId = id)
        }
    }

    fun navigateToMarketPlaceOwnerServiceDone() {
        _homeState.update {
            it.copy(navigateToMarketPlaceOwnerService = false)
        }
    }

    fun navigateToExploreProduct(catId: Int, catName: String) {
        _homeState.update {
            it.copy(
                navigateToExploreProduct = true,
                exploreSubCatId = catId,
                exploreSubCatName = catName
            )
        }
    }
    fun setFromAndToDate(from: String, to: String)  {
        _homeState.update {
            it.copy(
                fromDate = from,
                toDate = to
            )
        }
    }
    fun setFromAndToDateLanguage(from: String, to: String)  {
        _homeState.update {
            it.copy(
                fromDateLang = from,
                toDateLang = to
            )
        }
    }

    fun navigateToExploreProductDone() {
        _homeState.update {
            it.copy(navigateToExploreProduct = false)
        }
    }

    fun navigateToExploreService(catId: Int, catName: String) {
        _homeState.update {
            it.copy(
                navigateToExploreService = true,
                exploreSubCatId = catId,
                exploreSubCatName = catName
            )
        }
    }

    fun navigateToExploreServiceDone() {
        _homeState.update {
            it.copy(navigateToExploreService = false)
        }
    }

    fun navigateToTermsAndCondition() {
        _homeState.update {
            it.copy(navigateToTermsAndCondition = true)
        }
    }

    fun navigateToTermsAndConditionDone() {
        _homeState.update {
            it.copy(navigateToTermsAndCondition = false)
        }
    }


    fun setTermsAndCondition(boolean: Boolean) {
        _homeState.update {
            it.copy(isEnteredToTermsAndCondition = boolean)
        }
    }
    fun setVisibilityIsRental(boolean: Boolean) {
        _homeState.update {
            it.copy(setVisibilityIsRental = boolean)
        }
    }

    fun setHighest(boolean: Boolean) {
        _homeState.update {
            it.copy(priceHighest = boolean)
        }
    }

    fun setLowest(boolean: Boolean) {
        _homeState.update {
            it.copy(priceLowest = boolean)
        }
    }

    fun setLisCat(list: List<Int>) {
        _homeState.update {
            it.copy(listCat = list)
        }
    }

    fun setLisBrand(list: List<Int>) {
        _homeState.update {
            it.copy(listBrand = list)
        }
    }

    fun getSearchData(boolean: Boolean) {
        _homeState.update {
            it.copy(getSearchData = boolean)
        }
    }

    fun showSurveyImage(boolean: Boolean) {
        _homeState.update {
            it.copy(showSurveyImage = boolean)
        }
    }

    fun setCartList(list: List<CartUiList>) {
        _homeState.update {
            it.copy(cartUiListState = list)
        }
    }

    fun sendFCMToken(token: String, enabled: Boolean, isUpdate: Boolean) {
        _homeState.update {
            it.copy(
                error = null,
                isFCMSent = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isUpdate)
                    onSendFCMTokenSuccess(
                        manageProfileUseCase.updateFCMToken(token, enabled),
                        true,
                        token
                    )
                else
                    onSendFCMTokenSuccess(
                        manageProfileUseCase.sendFCMToken(token, enabled),
                        false,
                        token
                    )
            } catch (e: NoInternetException) {
                onSendFCMTokenFailure()
            } catch (e: Exception) {
                onSendFCMTokenFailure()
            }
        }
    }

    private fun onSendFCMTokenSuccess(response: BaseResponse, isUpdate: Boolean, token: String) {
        _homeState.update {
            it.copy(
                isLoading = false,
                error = null,
                isFCMSent = response.isSucceed,
            )
        }
        if (!response.isSucceed && !isUpdate)
            sendFCMToken(token = token, enabled = true, isUpdate = true)

    }

    private fun onSendFCMTokenFailure() {
        _homeState.update {
            it.copy(
                isLoading = false,
                error = null,
                isFCMSent = false,
            )
        }
    }

    fun resetFCM() {
        _homeState.update {
            it.copy(
                isFCMSent = false,
            )
        }
    }
}