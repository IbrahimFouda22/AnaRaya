package com.anaraya.data.mapper

import com.anaraya.data.dto.AddUpdateAddressDto
import com.anaraya.domain.entity.AddUpdateAddress

fun AddUpdateAddressDto.toEntity(): AddUpdateAddress {
    return AddUpdateAddress(
        message = message,
        isSucceed = isSucceed
    )
}