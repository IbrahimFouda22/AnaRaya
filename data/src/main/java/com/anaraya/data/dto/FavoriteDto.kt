package com.anaraya.data.dto

data class FavoriteDto(
    val data :List<ProductDtoItemList>,
    val isSucceed :Boolean,
)

//data class FavoriteDtoData(
//    val data : List<ProductDtoItemList>
//
//)
//data class FavoriteDtoItemList(
//    val id: Int,
//    val description: String,
//    val cat: String,
//    val maiN_CAT: String,
//    val brand: String,
//    val pricE_BEFORE: Double,
//    val pricE_AFTER: Double,
//    val discount: Double,
//    val availableQty: Int,
//    val specs1: String,
//    val specs2: String,
//    val stockImg: String?,
//    val isInBasket:Boolean
//)
