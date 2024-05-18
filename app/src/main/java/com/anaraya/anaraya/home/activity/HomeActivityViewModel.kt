package com.anaraya.anaraya.home.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.anaraya.anaraya.R
import com.anaraya.anaraya.home.cart.CartUiList
import com.anaraya.anaraya.home.services.store.my_items.ProductStoreItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    sharedPreferences: SharedPreferences,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
):ViewModel() {
    private val _homeState = MutableStateFlow(HomeActivityUiState())
    val homeState = _homeState as StateFlow<HomeActivityUiState>
    private val _navigateToCart = MutableStateFlow(false)
    val navigateToCart = _navigateToCart as StateFlow<Boolean>
    init {
        setProductInBasket(sharedPreferences.getInt(context.getString(R.string.productsinbasket),0))
    }
    fun setError(error:String?){
        _homeState.update {
            HomeActivityUiState(
                error = error
            )
        }
    }
    fun setProductInBasket(num:Int){
        _homeState.update {
            it.copy(
                productInBasket = num
            )
        }
    }
    fun getUserMyInfo(){
        _homeState.update {
            it.copy(
                getUserMyInfo = true
            )
        }
    }
    fun getUserMyInfoDone(){
        _homeState.update {
            it.copy(
                getUserMyInfo = false
            )
        }
    }
    fun getCart(){
        _homeState.update {
            it.copy(
                getCart = true
            )
        }
    }
    fun getCartDone(){
        _homeState.update {
            it.copy(
                getCart = false
            )
        }
    }
    fun getAddresses(){
        _homeState.update {
            it.copy(
                getAddresses = true
            )
        }
    }
    fun getAddressesDone(){
        _homeState.update {
            it.copy(
                getAddresses = false
            )
        }
    }
    fun getUserMoreInfo(){
        _homeState.update {
            it.copy(
                getUserMoreInfo = true
            )
        }
    }
    fun getUserMoreDone(){
        _homeState.update {
            it.copy(
                getUserMoreInfo = false
            )
        }
    }
    fun reloadClick(){
        _homeState.update {
            HomeActivityUiState(
                error = null,
                reloadClick = true
            )
        }
    }
    fun reloadClickDone(){
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

    fun navigateToSell(item: ProductStoreItemState?) {
        _homeState.update {
            it.copy(navigateToSell = true, itemSell = item)
        }
    }
    fun navigateToFavSell() {
        _homeState.update {
            it.copy(navigateToSell = false, itemSell = null)
        }
    }
    fun setAutoSearch(boolean: Boolean){
        _homeState.update {
            it.copy(openSearch = boolean)
        }
    }
    fun setHighest(boolean: Boolean){
        _homeState.update {
            it.copy(priceHighest = boolean)
        }
    }
    fun setLowest(boolean: Boolean){
        _homeState.update {
            it.copy(priceLowest = boolean)
        }
    }

    fun setLisCat(list: List<Int>){
        _homeState.update {
            it.copy(listCat = list)
        }
    }
    fun setLisBrand(list: List<Int>){
        _homeState.update {
            it.copy(listBrand = list)
        }
    }
    fun getSearchData(boolean: Boolean){
        _homeState.update {
            it.copy(getSearchData = boolean)
        }
    }

    fun setCartList(list: List<CartUiList>){
        _homeState.update {
            it.copy(cartUiListState = list)
        }
    }
}