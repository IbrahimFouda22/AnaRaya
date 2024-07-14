package com.anaraya.domain.usecase

import com.anaraya.domain.entity.SurveyBody
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageSurveysUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getSurveysStatus() = repo.getSurveysStatus()
    suspend fun getAllSurveys() = repo.getAllSurveys()
    suspend fun getSurvey(surveyId: Int) = repo.getSurvey(surveyId)
    suspend fun submitSurvey(surveyBody: SurveyBody) = repo.submitSurvey(surveyBody)
    suspend fun getSurveyImage() = repo.getSurveyImage()
}