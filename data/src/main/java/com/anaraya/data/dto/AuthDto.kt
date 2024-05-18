package com.anaraya.data.dto

data class AuthDto(
    val data:AuthDtoData?,
    val message:String?,
    val isSucceed:Boolean,
)
data class AuthDtoData(
    val token:String?,
    val productsInBasket:Int = 0,
    val profileImage:String?
)
