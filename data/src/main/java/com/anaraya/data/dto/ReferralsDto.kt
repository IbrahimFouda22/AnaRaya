package com.anaraya.data.dto

data class ReferralsDto(
    val data :List<ReferralDtoData>,
    val message: String?,
    val isSucceed: Boolean,
)

data class ReferralDtoData(
    val referralName:String,
    val referralPhoneNumber:String,
    val referralEmail:String?,
    val referalRelationShipId:Int,
    val referalRelationShip:String,
    val acceptedOrRejected:Boolean?,
)