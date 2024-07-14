package com.anaraya.data.mapper

import com.anaraya.data.dto.SurveyAnswerBodyDto
import com.anaraya.data.dto.SurveyBodyDto
import com.anaraya.domain.entity.SurveyBody


fun SurveyBody.toDto():SurveyBodyDto{
    return SurveyBodyDto(
        surveyId = surveyId,
        surveyAnswers = surveyAnswers.map {
            SurveyAnswerBodyDto(
                questionId = it.questionId,
                answerChoiceIds = it.answerChoiceIds,
                textBoxAnswer = it.textBoxAnswer
            )
        }
    )
}