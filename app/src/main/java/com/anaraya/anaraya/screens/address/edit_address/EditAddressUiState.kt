package com.anaraya.anaraya.screens.address.edit_address

import com.anaraya.anaraya.screens.shared_data.AddressUiState

data class EditAddressUiState(
    val isLoading: Boolean = false,
    val saveIsVisible: Boolean = false,
    val error: String? = null,
    val editAddressUiState: String? = null,
    val addressUiState: AddressUiState? =null
)

