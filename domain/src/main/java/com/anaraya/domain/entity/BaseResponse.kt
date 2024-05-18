package com.anaraya.domain.entity

data class BaseResponse(
    val isSucceed: Boolean,
    val message: String?,
    val data: String?,
    val statusCode: Int,
)
