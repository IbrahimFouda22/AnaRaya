package com.anaraya.data.mapper

import com.anaraya.data.dto.FeedBackDto
import com.anaraya.domain.entity.FeedBack

fun FeedBackDto.toEntity(): FeedBack{
    return FeedBack(
        isSucceed, message, data
    )
}