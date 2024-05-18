package com.anaraya.domain.entity

data class PromoCode(
    val data :List<PromoCodeData>,
    val isSucceed :Boolean,
)

data class PromoCodeData(
    val code: String,
    val name: String,
    val discount: Int,
    val validTill: String,
    val percentageOrFixedAmount:Boolean
)
