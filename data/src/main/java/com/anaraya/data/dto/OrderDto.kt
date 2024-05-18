package com.anaraya.data.dto

data class OrderDto(
    val isSucceed: Boolean,
    val message: String,
    val data: List<OrderDtoData>
)

data class OrderDtoData(
    val orderId: Int,
    val currentStatus: Int,
    val orderPlacedDate: String?,
    val orderAcceptedDate: String?,
    val orderOutForDeliveryDate: String?,
    val orderDeliveredDate: String?,
    val orderCancellDate: String?,
    val userConfirmOrder: Boolean,
    val isCancelled: Boolean,
    val productsDescription: List<String?>,
)
