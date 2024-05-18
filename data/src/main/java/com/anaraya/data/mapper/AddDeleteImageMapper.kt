package com.anaraya.data.mapper
import com.anaraya.data.dto.AddDeleteImageDto
import com.anaraya.domain.entity.AddDeleteImage

fun AddDeleteImageDto.toEntity(): AddDeleteImage {
    return AddDeleteImage(
        message, isSucceed
    )
}