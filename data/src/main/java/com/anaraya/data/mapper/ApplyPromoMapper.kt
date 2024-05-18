package com.anaraya.data.mapper

import com.anaraya.data.dto.ApplyPromoDto
import com.anaraya.domain.entity.ApplyPromo

fun ApplyPromoDto.toEntity(): ApplyPromo{
    return ApplyPromo(
        isSucceed, message, data.toEntity()
    )
}