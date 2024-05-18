package com.anaraya.data.mapper

import com.anaraya.data.dto.CheckOutDto
import com.anaraya.domain.entity.CheckOut
import com.anaraya.domain.entity.CheckOutData

fun CheckOutDto.toEntity(): CheckOut {
    return CheckOut(
        isSucceed = isSucceed,
        message = message,
        data = CheckOutData(
            addressIsUserAddressOrCompanyAddress = data.addressIsUserAddressOrCompanyAddress,
            deliveryAddressId = data.deliveryAddressId,
            deliveryAddressLabel = data.deliveryAddressLabel,
            promoCode = data.promoCode,
            promoCodeName = data.promoCodeName,
            promoCodeDiscount = data.promoCodeDiscount,
            totalCost = data.totalCost,
            paymentMethods = data.paymentMethods
        )
    )
}