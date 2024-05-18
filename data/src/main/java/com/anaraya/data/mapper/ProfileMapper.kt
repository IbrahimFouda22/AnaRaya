package com.anaraya.data.mapper

import com.anaraya.data.dto.ProfileDto
import com.anaraya.domain.entity.Address
import com.anaraya.domain.entity.CompanyAddressItem
import com.anaraya.domain.entity.Profile
import com.anaraya.domain.entity.ProfileData

fun ProfileDto.toEntity(): Profile {
    return Profile(
        isSucceed = isSucceed,
        message = message,
        data = ProfileData(
            name = data.name,
            phoneNumber = data.phoneNumber,
            email = data.email,
            profileImage = data.profileImage,
            phoneNumberConfirmed = data.phoneNumberConfirmed,
            userDefaultAddress =
            data.userDefaultAddress?.let {
                Address(
                    id = it.id,
                    governorate = data.userDefaultAddress.governorate,
                    district = data.userDefaultAddress.district,
                    landmark = data.userDefaultAddress.landmark,
                    address = data.userDefaultAddress.address,
                    addressLabel = data.userDefaultAddress.adressLabel,
                    defaultAddress = data.userDefaultAddress.defaultAddress,
                    street = data.userDefaultAddress.street,
                    building = data.userDefaultAddress.building
                )
            },
            companyDefaultAddress = data.companyDefaultAddress?.let {
                CompanyAddressItem(
                    id = data.companyDefaultAddress.id,
                    companyName = data.companyDefaultAddress.companyName,
                    office = data.companyDefaultAddress.office,
                    governorate = data.companyDefaultAddress.governorate,
                    dayOfDelivery = data.companyDefaultAddress.dayOfDelivery,
                    defaultAddress = data.companyDefaultAddress.defaultAddress,
                    deliveryFrom = data.companyDefaultAddress.deliveryFrom,
                    deliveryTo = data.companyDefaultAddress.deliveryTo
                )
            }
        ),
    )
}