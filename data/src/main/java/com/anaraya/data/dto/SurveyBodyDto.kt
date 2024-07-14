package com.anaraya.data.dto

data class SurveyBodyDto(
    val surveyId: Int,
    val surveyAnswers: List<SurveyAnswerBodyDto>,
)

data class SurveyAnswerBodyDto(
    val questionId: Int,
    val answerChoiceIds: List<String>,
    val textBoxAnswer: String,
)