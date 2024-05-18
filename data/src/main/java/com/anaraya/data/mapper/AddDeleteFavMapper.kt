package com.anaraya.data.mapper

import com.anaraya.data.dto.AddDeleteFavDto
import com.anaraya.domain.entity.AddDeleteFav

fun AddDeleteFavDto.toEntity(): AddDeleteFav {
    return AddDeleteFav(
        data, message, isSucceed
    )
}