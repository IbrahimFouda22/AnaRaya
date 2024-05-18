package com.anaraya.anaraya.landing.splash

data class SplashUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateToHome: Boolean = true
)
