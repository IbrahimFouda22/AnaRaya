package com.anaraya.data.dto

data class PromoCodeDto(
    val data :List<PromoCodeDtoData>,
    val isSucceed :Boolean,
)

data class PromoCodeDtoData(
    val code: String,
    val name: String,
    val discount: Int,
    val validTill: String,
    val percentageOrFixedAmount:Boolean
)
