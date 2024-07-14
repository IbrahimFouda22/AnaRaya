package com.anaraya.domain.entity

data class ServiceStoreItemList(
    val id: Int,
    val category: String,
    val categoryId: Int,
    val subCategory: String,
    val subCategoryId: Int,
    val title: String,
    val serviceDescription: String,
    val price: Double,
    val location: String,
    val serviceImageUrl: String? = null,
    val userAction: Int,
    val numberOfBuyers: Int,
    val customerInformation: List<ServiceCustomerInformation>,
    val customerWantsToRent: List<Int>,
)

data class ServiceCustomerInformation(
    val listiningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrid: String,
    val phoneNumber: String,
    val rentFrom: String,
    val rentTo: String,
    val  email: String,
    val rentStatus: Int,
    val payMethod: Int
)
