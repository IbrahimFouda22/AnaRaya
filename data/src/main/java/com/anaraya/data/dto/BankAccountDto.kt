package com.anaraya.data.dto

data class BankAccountDto(
    val isSucceed: Boolean,
    val message: String?,
    val data: BankAccountDetailsDto,
    val statusCode: Int,
)
data class BankAccountDetailsDto(
    val iban: String?,
    val holdingName: String?,
    val branch: String?,
)
