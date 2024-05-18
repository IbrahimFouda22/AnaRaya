package com.anaraya.domain.entity

data class ResetChangePass(
    val isSucceed: Boolean,
    val message: String?,
    val data:String? = null
)
