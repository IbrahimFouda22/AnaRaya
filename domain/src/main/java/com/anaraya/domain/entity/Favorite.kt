package com.anaraya.domain.entity

data class FavoriteDto(
    val data :FavoriteDtoData,
    val isSucceed :Boolean,
)

data class FavoriteDtoData(
    val data : List<Product>

)

