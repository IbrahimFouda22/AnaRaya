package com.anaraya.data.mapper

import com.anaraya.data.dto.PlaceOrderDto
import com.anaraya.domain.entity.PlaceOrder

fun PlaceOrderDto.toEntity():PlaceOrder{
    return PlaceOrder(
        isSucceed = isSucceed,
        message = message
    )
}