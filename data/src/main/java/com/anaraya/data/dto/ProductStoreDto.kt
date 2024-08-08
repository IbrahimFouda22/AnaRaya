package com.anaraya.data.dto

data class ProductStoreDto(
    val data: ProductStoreDtoData
)

data class ProductStoreDtoData(
    val itemsList: List<ProductStoreDtoItemList>

)

data class ProductStoreDtoDetails(
    val data: ProductStoreDtoItemList
)
data class ProductStoreDtoItemList(
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
    val isAnonymous:Boolean?,
    val isPaymentConfirmed: Boolean?,
    val handleDelivery:Boolean?,
    val customerInformation: CustomerInformation?,
)

data class CustomerInformation(
    val listiningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrid: String,
    val phoneNumber: String,
    val email: String?,
    val sellingStatus: Int?,
    val payMethod: Int?
)

data class ProductStoreDtoDetailsForCustomer(
    val data: ProductStoreDtoItemListCustomer
)
data class ProductStoreDtoItemListCustomer(
    val id: Int,
    val category: String,
    val subCategory: String,
    val condition: Int,
    val title: String,
    val itemDescription: String,
    val price: Double,
    val location: String,
    val productImageUrl: String? = null,
    val sellerName: String?,
    val sellerPhoneNumber: String?,
    val sellingStatus: Int?,
    val isUserProduct:Boolean,
    val isAnonymous:Boolean,
)


