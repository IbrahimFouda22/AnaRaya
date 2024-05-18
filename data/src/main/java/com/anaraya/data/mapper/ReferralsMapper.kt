package com.anaraya.data.mapper

import com.anaraya.data.dto.ReferralsDto
import com.anaraya.domain.entity.ReferralData
import com.anaraya.domain.entity.Referrals

fun ReferralsDto.toEntity(): Referrals {
    return Referrals(
        message = message,
        isSucceed = isSucceed,
        data = data.map {
            ReferralData(
                referralName = it.referralName,
                referralEmail = it.referralEmail,
                referralPhoneNumber = it.referralPhoneNumber,
                referralRelationShip = it.referalRelationShip,
                referralRelationShipId = it.referalRelationShipId,
                acceptedOrRejected = it.acceptedOrRejected
            )
        }
    )
}