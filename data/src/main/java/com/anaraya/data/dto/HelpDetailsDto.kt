package com.anaraya.data.dto

data class HelpDetailsDto(
    val isSucceed: Boolean,
    val message: String?,
    val data: List<HelpDetailsDtoData>
)

data class HelpDetailsDtoData(
    val problem: String,
    val answer: String,
)