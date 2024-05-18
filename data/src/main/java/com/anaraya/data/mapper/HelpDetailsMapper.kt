package com.anaraya.data.mapper

import com.anaraya.data.dto.HelpDetailsDto
import com.anaraya.domain.entity.HelpDetails
import com.anaraya.domain.entity.HelpDetailsData

fun HelpDetailsDto.toEntity(): HelpDetails{
    return HelpDetails(
        isSucceed,message,data = data.map {
            HelpDetailsData(it.problem,it.answer)
        }
    )
}