package com.anaraya.data.mapper

import com.anaraya.data.dto.CheckAuthDto
import com.anaraya.domain.entity.CheckAuth

fun CheckAuthDto.toEntity():CheckAuth{
    return CheckAuth(isSucceed, message)
}