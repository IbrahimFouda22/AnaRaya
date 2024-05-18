package com.anaraya.anaraya

data class MainActivityUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val reloadClick: Boolean = false,
)
