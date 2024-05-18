package com.anaraya.data.mapper

import com.anaraya.data.dto.ContactNumberDto
import com.anaraya.domain.entity.ContactNumber

fun ContactNumberDto.toEntity():ContactNumber{
    return ContactNumber(
        isSucceed, data, message
    )
}