package com.anaraya.domain.entity

data class Profile(
    val message: String?,
    val isSucceed: Boolean,
    val data: ProfileData
)

data class ProfileData(
    val name: String,
    val email: String?,
    val phoneNumber: String,
    val profileImage: String? = null,
    val dateOfBirth: String? = null,
    val userDefaultAddress: Address? = null,
    val companyDefaultAddress: CompanyAddressItem? = null,
    val phoneNumberConfirmed: Boolean = false,
    val gender: Int = 0,
)