package com.anaraya.data.mapper

import com.anaraya.data.dto.OrderDto
import com.anaraya.domain.entity.Order
import com.anaraya.domain.entity.OrderData

fun OrderDto.toEntity(): Order {
    return Order(
        isSucceed = isSucceed,
        message = message,
        data = data.map {
            OrderData(
                orderId = it.orderId,
                orderAcceptedDate = it.orderAcceptedDate,
                orderCancelDate = it.orderCancellDate,
                orderDeliveredDate = it.orderDeliveredDate,
                orderPlacedDate = it.orderPlacedDate,
                orderOutForDeliveryDate = it.orderOutForDeliveryDate,
                userConfirmOrder = it.userConfirmOrder,
                productsDescription = it.productsDescription,
                currentStatus = it.currentStatus,
                isCancelled = it.isCancelled
            )
        }
    )
}