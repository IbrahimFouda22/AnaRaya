package com.anaraya.data.dto

data class RefreshTokenDto(
    val data: RefreshTokenDtoDetails?,
)

data class RefreshTokenDtoDetails(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isDeleted: Boolean?,
)
