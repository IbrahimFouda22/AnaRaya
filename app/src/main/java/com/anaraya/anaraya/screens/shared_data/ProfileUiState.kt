package com.anaraya.anaraya.screens.shared_data
import java.io.Serializable
data class ProfileUiState(
    val name: String,
    val email: String?,
    val phoneNumber: String,
    val dateOfBirth: String? = null,
    val profileImage: String? = null,
    val defaultAddressUiState: AddressUiState? = null,
    val phoneNumberConfirmed: Boolean = false,
    val gender: Int = 0,
    val visibilityEmail: Boolean = false
) : Serializable
