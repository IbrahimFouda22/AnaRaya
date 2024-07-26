package com.anaraya.domain.entity

data class BankAccount(
    val isSucceed: Boolean,
    val message: String?,
    val data: BankAccountDetails,
    val statusCode: Int,
)
data class BankAccountDetails(
    val iban: String,
    val holdingName: String,
    val branch: String,
)
