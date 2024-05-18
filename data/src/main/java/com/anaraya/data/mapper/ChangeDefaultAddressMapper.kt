package com.anaraya.data.mapper

import com.anaraya.data.dto.ChangeDefaultAddressDto
import com.anaraya.domain.entity.ChangeDefaultAddress

fun ChangeDefaultAddressDto.toEntity(): ChangeDefaultAddress {
    return ChangeDefaultAddress(
        message = message,
        statusCode = statusCode,
        isSucceed = isSucceed
    )
}