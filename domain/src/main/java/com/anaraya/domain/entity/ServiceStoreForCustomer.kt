package com.anaraya.domain.entity

data class ServiceStoreForCustomer(
    val id: Int,
    val category: String,
    val subCategory: String,
    val title: String,
    val serviceDescription: String,
    val price: Double,
    val location: String,
    val serviceImageUrl: String? = null,
    val sellerName: String,
    val sellerPhoneNumber: String,
    val baseRentFrom: String,
    val baseRentTo: String,
    val rentFrom: String,
    val rentTo: String,
    val rentStatus: Int,
    val isUserService:Boolean,
    val itIsARent:Boolean,
    val rentedPeriods:List<RentedPeriods>
)
data class RentedPeriods(
    val rentFrom:String,
    val rentTo:String,
)