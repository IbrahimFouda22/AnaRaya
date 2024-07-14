package com.anaraya.domain.entity

data class SurveyImage(
    val data: SurveyImageDetails,
    val isSucceed: Boolean,
)

data class SurveyImageDetails(
    val imageUrl: String,
    val surveyId: Int,
)
