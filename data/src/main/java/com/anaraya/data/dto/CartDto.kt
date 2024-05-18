package com.anaraya.data.dto

data class CartDto(
    val data: CartDtoData,
    val isSucceed: Boolean,
    val message: String?,
)


data class CartDtoData(
    val stocksList: List<CartDtoStockList>,
    val cartTotal: Double,
    val deliveryFee: Double,
    val totalAmount: Double,
    val promoCodeDiscount: Double,
    val ammountToTakeFreeDelivery: Double,
    val hasAddress: Boolean,
)

data class CartDtoStockList(
    val id: Int,
    val cat: String,
    val maiN_CAT: String,
    val brand: String,
    val pricePerOne: Double,
    val totalPrice: Double,
    val productAvailableQty: Int,
    val deliveryFee: Double,
    val qty: Int,
    val specs1: String,
    val specs2: String,
    val stockImg: String? = null

)
