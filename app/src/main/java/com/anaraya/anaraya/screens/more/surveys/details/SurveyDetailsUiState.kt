package com.anaraya.anaraya.screens.more.surveys.details

import com.anaraya.domain.entity.Survey
import com.anaraya.domain.entity.SurveyAnswerBody
import com.anaraya.domain.entity.SurveyBody

data class SurveyDetailsUiState(
    val isLoading: Boolean = false,
    val isSucceedSubmitSurvey: Boolean = false,
    val msgSubmitSurvey: String? = null,
    val error: String? = null,
    val surveyTitle: String = "",
    val surveyId: Int = -1,
    val survey: SurveyDetailsState? = null,
    val mapMultipleChoice: Map<String,String> = mutableMapOf(),
    val mapCheckBoxes: Map<String,List<String>> = mutableMapOf(),
    val mapTextBox: Map<String,String> = mutableMapOf(),
)

data class SurveyDetailsState(
    val surveyId: Int,
    val title: String,
    val startDate: String,
    val endDate: String,
    val surveyQuestions: List<SurveyQuestionUiState>,
)

data class SurveyQuestionUiState(
    val questionId: Int,
    val question: String,
    val answerType: Int,
    val questionChoices: List<SurveyChoicesUiState>,
)

data class SurveyChoicesUiState(
    val choiceId: String,
    val choice: String,
)

data class SurveyBodyUiState(
    val surveyId: Int,
    val surveyAnswers: List<SurveyAnswerBodyUiState>,
)

data class SurveyAnswerBodyUiState(
    val questionId: Int,
    val answerChoiceIds: List<String>,
    val textBoxAnswer: String,
)


fun Survey.toUiState(): SurveyDetailsState {
    return SurveyDetailsState(
        surveyId = data.surveyId,
        startDate = data.startDate,
        endDate = data.endDate,
        title = data.title,
        surveyQuestions = data.surveyQuestions.map { question ->
            SurveyQuestionUiState(
                questionId = question.questionId,
                question = question.question,
                answerType = question.answerType,
                questionChoices = question.questionChoices.map { choice ->
                    SurveyChoicesUiState(
                        choiceId = choice.choiceId,
                        choice = choice.choice,
                    )
                }
            )
        }
    )
}

fun SurveyBodyUiState.toEntity(): SurveyBody {
    return SurveyBody(
        surveyId = surveyId,
        surveyAnswers = surveyAnswers.map {
            SurveyAnswerBody(
                questionId = it.questionId,
                answerChoiceIds = it.answerChoiceIds,
                textBoxAnswer = it.textBoxAnswer
            )
        }
    )
}