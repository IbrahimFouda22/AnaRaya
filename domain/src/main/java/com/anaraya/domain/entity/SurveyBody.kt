package com.anaraya.domain.entity

data class SurveyBody(
    val surveyId: Int,
    val surveyAnswers: List<SurveyAnswerBody>,
)

data class SurveyAnswerBody(
    val questionId: Int,
    val answerChoiceIds: List<String>,
    val textBoxAnswer: String,
)