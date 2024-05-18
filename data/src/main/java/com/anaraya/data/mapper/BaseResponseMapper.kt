package com.anaraya.data.mapper

import com.anaraya.data.dto.BaseResponseDto
import com.anaraya.domain.entity.BaseResponse

fun BaseResponseDto.toEntity():BaseResponse{
    return BaseResponse(isSucceed, message,data, statusCode)
}