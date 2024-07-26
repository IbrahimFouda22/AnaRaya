package com.anaraya.anaraya.screens.services.store.product.my_items.item_sold

import com.anaraya.anaraya.screens.services.store.product.my_items.ProductStoreItemState


data class ItemSoldUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val product: ProductStoreItemState? = null,
    val error: String? = null,
)
