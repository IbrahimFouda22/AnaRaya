package com.anaraya.data.mapper

import com.anaraya.data.dto.AddRemoveCartDto
import com.anaraya.domain.entity.AddRemoveCart

fun AddRemoveCartDto.toEntity(): AddRemoveCart {
    return AddRemoveCart(
        isSucceed = isSucceed,
        message = message
    )
}