package com.anaraya.data.mapper

import com.anaraya.data.dto.SurveyImageDto
import com.anaraya.domain.entity.SurveyImage
import com.anaraya.domain.entity.SurveyImageDetails

fun SurveyImageDto.toEntity(): SurveyImage {
    return SurveyImage(
        isSucceed = isSucceed,
        data = SurveyImageDetails(
            imageUrl = data?.imageUrl ?: "",
            surveyId = data?.surveyId ?: -1
        )
    )
}