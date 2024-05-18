package com.anaraya.data.dto

data class AddressDto(
    val data: List<AddressDtoData>,
    val isSucceed:Boolean
)

data class AddressDtoData(
    val id: String,
    val adressLabel: String,
    val governorate: String,
    val district: String,
    val address: String,
    val street: String,
    val building: String,
    val landmark: String,
    val defaultAddress: Boolean,
)
