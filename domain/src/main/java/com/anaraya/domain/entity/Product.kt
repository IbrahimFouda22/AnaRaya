package com.anaraya.domain.entity

data class Product(
    val id: Int,
    val description: String,
    val category: String,
    val mainCategory: String,
    val brand: String,
    val priceBefore: Double,
    val priceAfter: Double,
    val discount: Double,
    val availableQty: Int,
    val name: String,
    val specs1: String,
    val specs2: String,
    val image: String? = null,
    val images: List<String?>? = null,
    val favouriteStock: Boolean? = null,
    val inBasket: Boolean,
    val qtyInBasket: Int? = null,
    val limitQtyForUserPerMonth: Int,
    val qtyUsedFromLimit: Int,
)
