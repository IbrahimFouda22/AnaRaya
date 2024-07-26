package com.anaraya.data.mapper

import com.anaraya.data.dto.BankAccountDto
import com.anaraya.domain.entity.BankAccount
import com.anaraya.domain.entity.BankAccountDetails

fun BankAccountDto.toEntity(): BankAccount {
    return BankAccount(
        isSucceed = isSucceed,
        message = message,
        data = BankAccountDetails(
            iban = data.iban ?: "",
            branch = data.branch ?: "",
            holdingName = data.holdingName ?: ""
        ),
        statusCode = statusCode
    )
}