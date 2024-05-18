package com.anaraya.anaraya.home.favorite

import com.anaraya.anaraya.home.shared_data.ProductUiState

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val isSucceed:Boolean=false,
    val showEmptyFav:Boolean=false,
    val navigateToHome:Boolean=false,
    val error: String? = null,
    val deleteMsg: String? = null,
    val addAllToBasketMsg: String? = null,
    val numAdded: Int = 0,
    val products: List<ProductUiState> = emptyList()
)
