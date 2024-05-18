package com.anaraya.data.mapper

import com.anaraya.data.dto.CompanyGovernorateDto
import com.anaraya.domain.entity.CompanyGovernorate

fun CompanyGovernorateDto.toEntity(): CompanyGovernorate {
    return CompanyGovernorate(
        isSucceed = isSucceed, message = message, data = data
    )
}