package com.anaraya.domain.entity

data class SurveysStatus(
    val isSucceed: Boolean,
    val message: String?,
    val data: Boolean,
    val statusCode: Int,
)
