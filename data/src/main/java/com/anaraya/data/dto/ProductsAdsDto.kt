package com.anaraya.data.dto

data class ProductsAdsDto(
    val data: List<ProductsAdsDtoData>
)

data class ProductsAdsDtoData(
    val id: Int,
    val adsImageUrl: String? = null,
    val adsLink: String? = null,
    val adsType: Int,
)
