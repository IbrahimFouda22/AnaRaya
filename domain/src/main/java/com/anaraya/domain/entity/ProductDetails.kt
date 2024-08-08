package com.anaraya.domain.entity

data class ProductDetails(
    val data: ProductDetailsData,
)

data class ProductDetailsData(
    val id: Int,
    val description: String,
    val cat: String,
    val mainCat: String,
    val brand: String,
    val priceAfter: Double,
    val priceBefore: Double,
    val discount: Double,
    val availableQty: Int,
    val specs1: String,
    val specs2: String,
    val stockImages: List<String?>?=null,
    val favouriteStock: Boolean,
    val inBasket: Boolean,
    val notifyMe: Boolean,
    val qtyInBasket: Int,
    val isInLoyaltyBasket: Boolean,
    val qtyInLoyaltyBasket: Int,
    val pointInRedeem: Double,
    val limitQtyForUserPerMonth: Int,
    val qtyUsedFromLimit: Int,
    val recommendedStocks: List<Product>,
//    val trending: Boolean,
//    val ads: Boolean,
//    val hide: Boolean
)

data class ImageDto(
    //val id:Int,
    val image:String?
)
