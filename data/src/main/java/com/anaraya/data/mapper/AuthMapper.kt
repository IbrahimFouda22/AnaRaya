package com.anaraya.data.mapper

import com.anaraya.data.dto.AuthDto
import com.anaraya.domain.entity.Auth

fun AuthDto.toEntity() = Auth(
    token = data?.token,
    refreshToken = data?.refreshToken,
    isSucceed = isSucceed,
    message = message,
    productsInBasket = data?.productsInBasket ?: 0,
    isDeleted = data?.isDeleted ?: false
)
