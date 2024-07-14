package com.anaraya.data.mapper

import com.anaraya.data.dto.SurveyDto
import com.anaraya.data.dto.SurveysDto
import com.anaraya.domain.entity.Survey
import com.anaraya.domain.entity.SurveyChoices
import com.anaraya.domain.entity.SurveyDetails
import com.anaraya.domain.entity.SurveyQuestion
import com.anaraya.domain.entity.Surveys

fun SurveysDto.toEntity(): Surveys {
    return Surveys(
        message = message, isSucceed = isSucceed, statusCode = statusCode, data = data.map {
            SurveyDetails(
                surveyId = it.surveyId,
                startDate = it.startDate,
                endDate = it.endDate,
                title = it.title,
                surveyQuestions = it.surveyQuestions.map { question ->
                    SurveyQuestion(
                        questionId = question.questionId,
                        question = question.question,
                        answerType = question.answerType,
                        questionChoices = question.questionChoices.map { choice ->
                            SurveyChoices(
                                choiceId = choice.choiceId,
                                choice = choice.choice,
                            )
                        }
                    )
                }
            )
        }
    )
}

fun SurveyDto.toEntity(): Survey {
    return Survey(
        message = message, isSucceed = isSucceed, statusCode = statusCode,
        data =
        SurveyDetails(
            surveyId = data.surveyId,
            startDate = data.startDate,
            endDate = data.endDate,
            title = data.title,
            surveyQuestions = data.surveyQuestions.map { question ->
                SurveyQuestion(
                    questionId = question.questionId,
                    question = question.question,
                    answerType = question.answerType,
                    questionChoices = question.questionChoices.map { choice ->
                        SurveyChoices(
                            choiceId = choice.choiceId,
                            choice = choice.choice,
                        )
                    }
                )
            }

        )
    )
}

