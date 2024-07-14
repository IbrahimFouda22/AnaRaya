package com.anaraya.anaraya.screens.services.store.product.my_items.item_details

import com.anaraya.anaraya.screens.services.store.product.my_items.ProductStoreItemState


data class ItemDetailsProductUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val product: ProductStoreItemState? = null,
    val isSucceedRequestToDelete: Boolean = false,
    val visibilitySellerInfo: Boolean = false,
    val isSucceedRequestToBuy: Boolean = false,
    val requestToBuyMessage: String? = null,
    val error: String? = null,
    val requestToDeleteMsg: String? = null,
    val sellingStatus: Int = 0,
)
