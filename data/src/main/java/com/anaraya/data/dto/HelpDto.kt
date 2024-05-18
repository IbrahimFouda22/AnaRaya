package com.anaraya.data.dto

data class HelpDto(
    val isSucceed: Boolean,
    val message: String?,
    val data: List<HelpDtoData>
)

data class HelpDtoData(
    val id: Int,
    val topic: String?
)
