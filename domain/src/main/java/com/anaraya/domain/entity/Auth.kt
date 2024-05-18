package com.anaraya.domain.entity

data class Auth(
    val token: String?,
    val message:String?,
    val productsInBasket:Int = 0,
    val isSucceed:Boolean
)
