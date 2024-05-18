package com.anaraya.data.dto

data class CheckOutDto(
    val data: CheckOutDtoData,
    val message: String?,
    val isSucceed: Boolean
)

data class CheckOutDtoData(
    val addressIsUserAddressOrCompanyAddress: Boolean?,
    val deliveryAddressLabel: String?,
    val deliveryAddressId: String?,
    val promoCodeName: String?,
    val promoCode: String?,
    val promoCodeDiscount: Double,
    val totalCost: Double,
    val paymentMethods: List<String>,
)
