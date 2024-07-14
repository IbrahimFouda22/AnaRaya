package com.anaraya.anaraya.screens.more.promo

import com.anaraya.domain.entity.PromoCodeData


data class PromoCodeUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val error: String? = null,
    val promoUiItem: List<PromoCodeUiItem> = emptyList()
)

data class PromoCodeUiItem(
    val code: String,
    val name: String,
    val discount: Int,
    val validTill: String,
    val percentageOrFixedAmount:Boolean
)

fun PromoCodeData.toUiItem() = PromoCodeUiItem(
    code, name, discount, validTill, percentageOrFixedAmount
)
