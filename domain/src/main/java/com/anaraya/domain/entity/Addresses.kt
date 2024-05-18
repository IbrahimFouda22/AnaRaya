package com.anaraya.domain.entity

data class Addresses(
    val isSucceed: Boolean,
    val message: String?,
    val data: List<Address>,
    val companyAddresses: List<CompanyAddressItem>
)
