package com.anaraya.domain.entity

data class Help(
    val isSucceed: Boolean,
    val message: String?,
    val data: List<HelpData>
)

data class HelpData(
    val id: Int,
    val topic: String?
)
