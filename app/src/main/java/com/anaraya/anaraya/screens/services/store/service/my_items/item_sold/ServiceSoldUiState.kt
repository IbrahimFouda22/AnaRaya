package com.anaraya.anaraya.screens.services.store.service.my_items.item_sold

import com.anaraya.anaraya.screens.services.store.service.my_items.ProductStoreItemServiceState


data class ServiceSoldUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val product: ProductStoreItemServiceState? = null,
    val error: String? = null,
)
