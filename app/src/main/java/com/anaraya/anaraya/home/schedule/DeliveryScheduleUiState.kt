package com.anaraya.anaraya.home.schedule

data class DeliveryScheduleUiState(
    val isLoading: Boolean = false,
    val navigateToBack: Boolean = false,
    val error: String? = null,
    val deliveryScheduleImage: String? = null
)
