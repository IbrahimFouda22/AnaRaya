package com.anaraya.domain.entity


data class ExploreServices(
    val id: Int,
    val title: String,
    val serviceDescription: String,
    val price: Double,
    val location: String,
    val serviceImageUrl: String,
    val category: String,
    val subCategory: String,
    val rentFrom: String,
    val rentTo: String,
    val rentStatus: Int,
    val isUserService: Boolean,
)
