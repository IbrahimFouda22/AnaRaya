package com.anaraya.anaraya.screens.otp


data class OTPUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val error: String? = null,
    val msgUpdate: String? = null,
)
