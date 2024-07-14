package com.anaraya.data.dto

data class SurveysDto(
    val message:String?,
    val statusCode:Int,
    val isSucceed:Boolean,
    val data:List<SurveyDetailsDto>
)
data class SurveyDto(
    val message:String?,
    val statusCode:Int,
    val isSucceed:Boolean,
    val data:SurveyDetailsDto
)

data class SurveyDetailsDto(
    val surveyId:Int,
    val title:String,
    val startDate:String,
    val endDate:String,
    val surveyQuestions:List<SurveyQuestionDto>,
)

data class SurveyQuestionDto(
    val questionId:Int,
    val question:String,
    val answerType:Int,
    val questionChoices:List<SurveyChoicesDto>
)

data class SurveyChoicesDto(
    val choiceId:String,
    val choice:String,
)
