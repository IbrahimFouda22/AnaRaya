package com.anaraya.data.dto

data class ExploreProductsDto(
    val data: ExploreProductsDtoData,
)

data class ExploreProductsDtoData(
    val itemsList: List<ExploreProductsDtoDetails>,
)

data class ExploreProductsDtoDetails(
    val id: Int,
    val category: String,
    val subCategory: String,
    val condition: Int,
    val title: String,
    val itemDescription: String,
    val price: Double,
    val location: String,
    val productImageUrl: String,
    val sellerName:String?,
    val sellerPhoneNumber:String?,
    val sellingStatus: Int? = null,
    val isUserProduct: Boolean,
)
