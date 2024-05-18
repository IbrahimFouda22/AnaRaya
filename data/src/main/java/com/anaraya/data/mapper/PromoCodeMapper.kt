package com.anaraya.data.mapper

import com.anaraya.data.dto.PromoCodeDto
import com.anaraya.domain.entity.PromoCode
import com.anaraya.domain.entity.PromoCodeData

fun PromoCodeDto.toEntity(): PromoCode {
    return PromoCode(
        isSucceed = isSucceed,
        data = data.map {
            PromoCodeData(
                code = it.code,
                discount = it.discount,
                percentageOrFixedAmount = it.percentageOrFixedAmount,
                validTill = it.validTill,
                name = it.name
            )
        }
    )

}
