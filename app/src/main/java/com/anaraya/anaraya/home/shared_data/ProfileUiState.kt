package com.anaraya.anaraya.home.shared_data
import java.io.Serializable
data class ProfileUiState(
    val name: String,
    val email: String?,
    val phoneNumber: String,
    val profileImage: String? = null,
    val defaultAddressUiState: AddressUiState? = null,
    val phoneNumberConfirmed: Boolean = false,
) : Serializable
