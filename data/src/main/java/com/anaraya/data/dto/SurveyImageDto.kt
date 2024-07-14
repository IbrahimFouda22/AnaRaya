package com.anaraya.data.dto

data class SurveyImageDto(
    val data: SurveyImageDtoDetails?,
    val message: String?,
    val isSucceed: Boolean,
    val statusCode: Int,
)

data class SurveyImageDtoDetails(
    val imageUrl: String?,
    val surveyId: Int?,
)
