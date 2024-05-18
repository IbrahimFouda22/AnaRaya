package com.anaraya.domain.entity

data class Address(
    val id: String,
    val addressLabel: String,
    val governorate: String,
    val district: String,
    val address: String,
    val street: String,
    val building: String,
    val landmark: String,
    val defaultAddress: Boolean,
)
