package com.anaraya.anaraya.screens.order.get_order

import com.anaraya.domain.entity.OrderData


data class OrdersUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val ordersUiState: List<OrderUiStateData> = emptyList()
)

data class OrderUiStateData(
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
    val orderPlaced: Boolean = false,
    val orderAccepted: Boolean = false,
    val orderPreparing: Boolean = false,
    val orderOutForDelivery: Boolean = false,
    val orderDelivered: Boolean = false,
) {
    var expanded = false
    val cancellable = currentStatus == 0 && !isCancelled
}

fun OrderData.toUiState():OrderUiStateData{
    return OrderUiStateData(
        orderId = orderId,
        orderAcceptedDate = orderAcceptedDate,
        orderCancelDate = orderCancelDate,
        orderDeliveredDate = orderDeliveredDate,
        orderPlacedDate = orderPlacedDate,
        orderOutForDeliveryDate = orderOutForDeliveryDate,
        userConfirmOrder = userConfirmOrder,
        productsDescription = productsDescription,
        currentStatus = currentStatus,
        isCancelled = isCancelled,
        orderPlaced = currentStatus >= 0,
        orderAccepted = currentStatus >= 1,
        orderPreparing = currentStatus >= 2 ,
        orderOutForDelivery = currentStatus >= 3,
        orderDelivered = currentStatus >= 4,
    )
}
