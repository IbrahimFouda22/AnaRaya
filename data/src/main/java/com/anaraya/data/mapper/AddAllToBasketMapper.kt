package com.anaraya.data.mapper

import com.anaraya.data.dto.AddAllToBasketDto
import com.anaraya.domain.entity.AddAllToBasket

fun AddAllToBasketDto.toEntity(): AddAllToBasket {
    return AddAllToBasket(
        isSucceed, message, data
    )
}