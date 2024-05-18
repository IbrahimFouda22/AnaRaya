package com.anaraya.anaraya.home.total_cost

import com.anaraya.anaraya.home.shared_data.AddOrderUiState

data class TotalCostUiState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val message: String? = null,
    val isSucceedApplyPromo: Boolean = false,
    val messageApplyPromo: String? = null,
    val isSucceedDeletePromo: Boolean = false,
    val messageDeletePromo: String? = null,
    val navigateToOrder: Boolean = false,
    val navigateToMarket: Boolean = false,
    val addOrderUiState: AddOrderUiState? = null
)
