package com.anaraya.data.mapper

import com.anaraya.data.dto.CompanyDto
import com.anaraya.domain.entity.Company
import com.anaraya.domain.entity.CompanyData

fun CompanyDto.toEntity(): Company {
    return Company(
        isSucceed = isSucceed,
        message = message,
        data = data.map {
            CompanyData(
                id = it.id,
                name = it.name
            )
        }
    )
}