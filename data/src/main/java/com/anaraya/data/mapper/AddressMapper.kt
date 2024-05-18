package com.anaraya.data.mapper

import com.anaraya.data.dto.AddressDto
import com.anaraya.domain.entity.Address

fun AddressDto.toEntity(): List<Address> {
    return data.map {
        Address(
            id = it.id,
            addressLabel = it.adressLabel,
            governorate = it.governorate,
            district = it.district,
            address = it.address,
            street = it.street,
            building = it.building,
            landmark = it.landmark,
            defaultAddress = it.defaultAddress,
        )
    }
}