package com.anaraya.data.dto

data class ProfileDto(
    val message: String?,
    val isSucceed: Boolean,
    val data: ProfileDtoData
)

data class ProfileDtoData(
    val name: String,
    val email: String?,
    val phoneNumber: String,
    val dateOfBirth: String?,
    val gender: String?,
    val phoneNumberConfirmed: Boolean,
    val profileImage: String? = null,
    val userDefaultAddress: AddressDtoData? = null,
    val companyDefaultAddress: CompanyAddressItem? = null,
)
