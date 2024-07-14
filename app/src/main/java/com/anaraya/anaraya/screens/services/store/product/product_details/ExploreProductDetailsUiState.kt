package com.anaraya.anaraya.screens.services.store.product.product_details


data class ExploreProductDetailsUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val visibilitySellerInfo: Boolean = false,
    val isSucceedRequestToBuy: Boolean = false,
    val requestToBuyMessage: String? = null,
    val error: String? = null,
    val sellingStatus: Int = 0,
)
