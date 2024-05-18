package com.anaraya.data.mapper

import com.anaraya.data.dto.AboutUSDto
import com.anaraya.data.dto.PromoCodeDto
import com.anaraya.domain.entity.AboutUS
import com.anaraya.domain.entity.PromoCode
import com.anaraya.domain.entity.PromoCodeData

fun AboutUSDto.toEntity(): AboutUS {
    return AboutUS(isSucceed, data, message)

}
