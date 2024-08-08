package com.anaraya.domain.entity


data class ProductStore(
    val id: Int,
    val category: String,
    val categoryId: Int,
    val subCategory: String,
    val subCategoryId: Int,
    val condition: Int,
    val title: String,
    val itemDescription: String,
    val price: Double,
    val location: String,
    val productImageUrl: String? = null,
    val userAction: Int,
    val status: Int,
    val customerWantsToBuy: Int,
    val isAnonymous: Boolean,
    val isPaymentConfirmed: Boolean,
    val handleDelivery: Boolean,
    val customerInformation: CustomerInformation? = null,
)

data class CustomerInformation(
    val listeningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrId: String,
    val phoneNumber: String,
    val email: String,
    val sellingStatus: Int,
    val payMethod: Int,
)

data class ProductStoreForCustomer(
    val id: Int,
    val category: String,
    val subCategory: String,
    val condition: Int,
    val title: String,
    val itemDescription: String,
    val price: Double,
    val location: String,
    val productImageUrl: String? = null,
    val sellerName: String,
    val sellerPhoneNumber: String,
    val sellingStatus: Int,
    val isUserProduct:Boolean,
    val isAnonymous:Boolean,
)
