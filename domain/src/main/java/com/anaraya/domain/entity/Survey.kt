package com.anaraya.domain.entity

data class Surveys(
    val message:String?,
    val statusCode:Int,
    val isSucceed:Boolean,
    val data:List<SurveyDetails>
)
data class Survey(
    val message:String?,
    val statusCode:Int,
    val isSucceed:Boolean,
    val data:SurveyDetails
)

data class SurveyDetails(
    val surveyId:Int,
    val title:String,
    val startDate:String,
    val endDate:String,
    val surveyQuestions:List<SurveyQuestion>,
)

data class SurveyQuestion(
    val questionId:Int,
    val question:String,
    val answerType:Int,
    val questionChoices:List<SurveyChoices>
)

data class SurveyChoices(
    val choiceId:String,
    val choice:String,
)
