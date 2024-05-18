package com.anaraya.data.mapper

import com.anaraya.data.dto.HelpDto
import com.anaraya.domain.entity.Help
import com.anaraya.domain.entity.HelpData

fun HelpDto.toEntity(): Help {
    return Help(
        isSucceed = isSucceed, message = message, data = data.map {
            HelpData(
                id = it.id, topic = it.topic
            )
        }
    )
}