package com.anaraya.data.mapper

import com.anaraya.data.dto.AuthDto
import com.anaraya.domain.entity.Auth

fun AuthDto.toEntity() = Auth(
    token = data?.token,
    isSucceed = isSucceed,
    message = message,
    productsInBasket = data?.productsInBasket ?: 0
)
