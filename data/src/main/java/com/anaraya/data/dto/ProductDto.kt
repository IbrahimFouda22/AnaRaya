package com.anaraya.data.dto

data class ProductDto(
    val data: ProductDtoData,
    val message: String?,
)

data class ProductDtoData(
    val itemsList: List<ProductDtoItemList>,
)

data class ProductDtoItemList(
    val id: Int,
    val description: String,
    val cat: String,
    val maiN_CAT: String,
    val brand: String,
    val pricE_BEFORE: Double,
    val pricE_AFTER: Double,
    val discount: Double,
    val availableQty: Int,
    val specs1: String,
    val specs2: String,
    val stockImg: String? = null,
    val isInBasket: Boolean,
    val isInLoyaltyBasket: Boolean = false,
    val limitQtyForUserPerMonth: Int,
    val qtyUsedFromLimit: Int,
    val qtyInBasket: Int,
    val notifyMe: Boolean,
)
