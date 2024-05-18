package com.anaraya.data.mapper

import com.anaraya.data.dto.CompanyAddressDto
import com.anaraya.domain.entity.CompanyAddress
import com.anaraya.domain.entity.CompanyAddressData
import com.anaraya.domain.entity.CompanyAddressItem

fun CompanyAddressDto.toEntity():CompanyAddress{
    return CompanyAddress(
        isSucceed =isSucceed,
        message = message,
        data = CompanyAddressData(
            itemsList = data.itemsList.map {
                CompanyAddressItem(
                    id = it.id,
                    companyName = it.companyName,
                    office = it.office,
                    governorate = it.governorate,
                    dayOfDelivery = it.dayOfDelivery,
                    addedToFavouriteOrNot = it.addedToFavouriteOrNot,
                    deliveryFrom = it.deliveryFrom,
                    deliveryTo = it.deliveryTo,
                    defaultAddress = it.defaultAddress
                )
            }
        )
    )
}