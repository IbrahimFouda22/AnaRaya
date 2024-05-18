package com.anaraya.data.mapper

import com.anaraya.data.dto.ResetChangePassDto
import com.anaraya.domain.entity.ResetChangePass

fun ResetChangePassDto.toEntity(): ResetChangePass {
    return ResetChangePass(
        isSucceed, message, data
    )
}