package com.anaraya.anaraya.screens.more.surveys

import com.anaraya.domain.entity.SurveyDetails

data class SurveysUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val surveys: List<SurveyItemUiState> = emptyList(),
)

data class SurveyItemUiState(
    val surveyId:Int,
    val title: String,
)

fun SurveyDetails.toUiState():SurveyItemUiState{
    return SurveyItemUiState(
        surveyId = surveyId,
        title = title,
    )
}
