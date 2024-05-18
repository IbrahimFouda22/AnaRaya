package com.anaraya.domain.entity

data class CheckOut(
    val data: CheckOutData,
    val message: String?,
    val isSucceed: Boolean
)

data class CheckOutData(
    val addressIsUserAddressOrCompanyAddress: Boolean?,
    val deliveryAddressLabel: String?,
    val deliveryAddressId: String?,
    val promoCodeName: String?,
    val promoCode: String?,
    val promoCodeDiscount: Double,
    val totalCost: Double,
    val paymentMethods: List<String>,
)
