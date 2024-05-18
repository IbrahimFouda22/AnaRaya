package com.anaraya.domain.entity

data class Order(
    val isSucceed: Boolean,
    val message: String,
    val data: List<OrderData>
)

data class OrderData(
    val orderId: Int,
    val currentStatus: Int,
    val orderPlacedDate: String?,
    val orderAcceptedDate: String?,
    val orderOutForDeliveryDate: String?,
    val orderDeliveredDate: String?,
    val orderCancelDate: String?,
    val userConfirmOrder: Boolean,
    val isCancelled: Boolean,
    val productsDescription: List<String?>,
)
