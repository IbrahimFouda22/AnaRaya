package com.anaraya.data.dto

data class SurveysStatusDto(
    val isSucceed: Boolean,
    val message: String?,
    val data: Boolean,
    val statusCode: Int,
)
