package com.anaraya.domain.entity

data class Referrals(
    val data :List<ReferralData>,
    val message: String?,
    val isSucceed: Boolean,
)

data class ReferralData(
    val referralName:String,
    val referralPhoneNumber:String,
    val referralEmail:String?,
    val referralRelationShipId:Int,
    val referralRelationShip:String,
    val acceptedOrRejected:Boolean?,
)