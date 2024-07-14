package com.anaraya.data.mapper

import com.anaraya.data.dto.SurveysStatusDto
import com.anaraya.domain.entity.SurveysStatus

fun SurveysStatusDto.toEntity(): SurveysStatus {
    return SurveysStatus(isSucceed, message,data, statusCode)
}