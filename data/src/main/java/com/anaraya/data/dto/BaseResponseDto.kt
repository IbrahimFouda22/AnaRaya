package com.anaraya.data.dto

data class BaseResponseDto(
    val isSucceed: Boolean,
    val message: String?,
    val data: String?,
    val statusCode: Int,
)
