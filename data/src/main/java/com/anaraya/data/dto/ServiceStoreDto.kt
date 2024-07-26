package com.anaraya.data.dto

data class ServiceStoreDto(
    val data: ServiceStoreDtoData
)

data class ServiceStoreDtoData(
    val itemsList: List<ServiceStoreDtoItemList>
)
data class ServiceStoreDtoDetailsData(
    val data: ServiceStoreDtoItemList
)

data class ServiceStoreDtoItemList(
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
    val status: Int,
    val numberOfBuyers: Int,
    val isPaymentConfirmed: Boolean,
    val itIsARent: Boolean,
    val rentFrom: String?,
    val rentTo: String?,
    val customerInformation: List<ServiceCustomerInformationDto>,
    val customerWantsToRent: List<Int>,
)

data class ServiceCustomerInformationDto(
    val listiningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrid: String,
    val phoneNumber: String,
    val rentFrom: String?,
    val rentTo: String?,
    val email: String?,
    val rentStatus: Int?,
    val payMethod: Int?
)

data class ServiceStoreDtoDetailsForCustomer(
    val data: ServiceStoreDtoItemListCustomer
)
data class ServiceStoreDtoItemListCustomer(
    val id: Int,
    val category: String,
    val subCategory: String,
    val title: String,
    val serviceDescription: String,
    val price: Double,
    val location: String,
    val serviceImageUrl: String? = null,
    val sellerName: String?,
    val sellerPhoneNumber: String?,
    val baseRentFrom: String?,
    val baseRentTo: String?,
    val rentFrom: String?,
    val rentTo: String?,
    val rentStatus: Int?,
    val isUserService:Boolean,
    val itIsARent:Boolean,
)