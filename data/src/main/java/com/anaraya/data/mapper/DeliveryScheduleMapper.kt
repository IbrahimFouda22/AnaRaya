package com.anaraya.data.mapper

import com.anaraya.data.dto.DeliveryScheduleDto
import com.anaraya.domain.entity.DeliverySchedule

fun DeliveryScheduleDto.toEntity():DeliverySchedule{
    return DeliverySchedule(
        isSucceed, message, data
    )
}