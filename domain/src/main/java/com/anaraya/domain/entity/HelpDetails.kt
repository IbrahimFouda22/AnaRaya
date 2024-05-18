package com.anaraya.domain.entity

data class HelpDetails(
    val isSucceed: Boolean,
    val message: String?,
    val data: List<HelpDetailsData>
)

data class HelpDetailsData(
    val problem: String,
    val answer: String,
)