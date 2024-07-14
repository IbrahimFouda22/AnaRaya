package com.anaraya.anaraya.screens.more.family.family_referrals

import com.anaraya.domain.entity.ReferralData

data class FamilyReferralsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val listFamilyReferrals: List<ReferralsItemUiState> = emptyList()
)

data class ReferralsItemUiState(
    val referralName:String,
    val referralPhoneNumber:String,
    val referralEmail:String?,
    val referralRelationShipId:Int,
    val referralRelationShip:String,
    val acceptedOrRejected:Boolean?,
)

fun ReferralData.toUiState() = ReferralsItemUiState(
    referralName = referralName,
    referralEmail = referralEmail,
    referralPhoneNumber = referralPhoneNumber,
    referralRelationShip = referralRelationShip,
    referralRelationShipId = referralRelationShipId,
    acceptedOrRejected = acceptedOrRejected

)