package com.anaraya.data.dto

data class ProductDetailsDto(
    val data: ProductDetailsData,
)

data class ProductDetailsData(
    val id: Int,
    val description: String,
    val cat: String,
    val maiN_CAT: String,
    val brand: String,
    val pricE_BEFORE: Double,
    val pricE_AFTER: Double,
    val discount: Double,
    val availableQty: Int,
    val specs1: String,
    val specs2: String,
    val stockImages: List<String?>?=null,
    val favouriteStock: Boolean,
    val inBasket: Boolean,
    val notifyMe: Boolean,
    val qtyInBasket: Int,
    val limityQtyForUserPerMonth: Int,
    val isInLoyaltyBasket: Boolean,
    val qtyInLoyaltyBasket: Int,
    val pointInRedem: Double,
    val qtyUsedFromLimtit: Int,
    val recomendedStocks: List<ProductDtoItemList>,
//    val trending: Boolean,
//    val ads: Boolean,
//    val hide: Boolean
)

data class ImageDto(
    //val id:Int,
    val image:String?
)
