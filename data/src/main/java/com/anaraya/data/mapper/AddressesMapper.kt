package com.anaraya.data.mapper

import com.anaraya.data.dto.AddressesDto
import com.anaraya.domain.entity.Address
import com.anaraya.domain.entity.Addresses
import com.anaraya.domain.entity.Company
import com.anaraya.domain.entity.CompanyAddressItem

fun AddressesDto.toEntity(): Addresses {
    return Addresses(
        isSucceed = isSucceed,
        message = message,
        data = data.userAddresses.map {
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
        },
        companyAddresses = data.favouriteCompanyAddresses.map {
            CompanyAddressItem(
                id = it.id,
                defaultAddress = it.defaultAddress,
                deliveryTo = it.deliveryTo,
                deliveryFrom = it.deliveryFrom,
                dayOfDelivery = it.dayOfDelivery,
                office = it.office,
                governorate = it.governorate,
                companyName = it.companyName
            )
        }
    )
}