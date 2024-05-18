package com.anaraya.anaraya.home.activity

import com.anaraya.anaraya.home.cart.CartUiList
import com.anaraya.anaraya.home.services.store.my_items.ProductStoreItemState

data class HomeActivityUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val reloadClick: Boolean = false,
    val getUserMyInfo: Boolean = false,
    val getAddresses: Boolean = false,
    val getUserMoreInfo: Boolean = false,
    val getCart: Boolean = false,
    val navigateToFav: Boolean = false,
    val navigateToSell: Boolean = false,
    val openSearch: Boolean = false,
    val productInBasket: Int = 0,
    val priceHighest: Boolean = false,
    val priceLowest: Boolean = false,
    val getSearchData: Boolean = false,
    val listCat:List<Int> = emptyList(),
    val listBrand:List<Int> = emptyList(),
    val itemSell: ProductStoreItemState? = null,
    val cartUiListState: List<CartUiList> = emptyList(),
)
