package com.anaraya.domain.entity

data class CompanyAddress(
    val data: CompanyAddressData,
    val message: String?,
    val isSucceed: Boolean
)

data class CompanyAddressData(
    val itemsList: List<CompanyAddressItem>
)

data class CompanyAddressItem(
    val id: String,
    val office: String,
    val governorate: String,
    val companyName: String,
    val dayOfDelivery: Int,
    val deliveryFrom: String,
    val deliveryTo: String,
    val defaultAddress: Boolean,
    val addedToFavouriteOrNot: Boolean = false,
)
