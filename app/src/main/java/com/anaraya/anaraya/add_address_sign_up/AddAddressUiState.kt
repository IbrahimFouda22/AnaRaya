package com.anaraya.anaraya.add_address_sign_up

import com.anaraya.anaraya.authentication.AddressUiStateData


data class AddAddressSignUpUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSucceed: Boolean = false,
    val addressLabelError: Boolean = false,
    val governorateUserError: Boolean = false,
    val districtError: Boolean = false,
    val addressError: Boolean = false,
    val streetError: Boolean = false,
    val buildingError: Boolean = false,
    val landMarkError: Boolean = false,
    val addressUiStateData: AddressUiStateData? = null,
)

