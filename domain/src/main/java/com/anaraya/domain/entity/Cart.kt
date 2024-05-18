package com.anaraya.domain.entity

data class Cart(
    val data: CartData,
    val isSucceed: Boolean,
    val message: String?,
)

data class CartData(
    val data: List<CartDataList>,
    val cartTotal: Double,
    val cartDeliveryFee: Double,
    val cartTotalAmount: Double,
    val cartPromoCodeDiscount: Double,
    val cartAmountToTakeFreeDelivery: Double,
    val hasAddress: Boolean,
)

data class CartDataList(
    val id: Int,
    val pricePerOne: Double,
    val totalProductPrice: Double,
    val qty: Int,
    val specs1: String,
    val specs2: String,
    val stockImg: String? = null,
    val productAvailableQty: Int,
    val deliveryFee: Double,
)
