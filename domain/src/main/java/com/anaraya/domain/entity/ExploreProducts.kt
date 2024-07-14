package com.anaraya.domain.entity


data class ExploreProduct(
    val id:Int,
    val category:String,
    val subCategory:String,
    val condition:Int,
    val title:String,
    val itemDescription:String,
    val price:Double,
    val location:String,
    val productImageUrl:String,
    val sellerName:String,
    val sellerPhoneNumber:String,
    val sellingStatus:Int,
    val isUserProduct:Boolean,
)
