package com.anaraya.data.mapper

import com.anaraya.data.dto.EditInfoDto
import com.anaraya.domain.entity.EditInfo

fun EditInfoDto.toEntity():EditInfo{
    return EditInfo(
        isSucceed, message
    )
}