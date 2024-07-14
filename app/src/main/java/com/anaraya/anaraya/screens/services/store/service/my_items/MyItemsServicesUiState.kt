package com.anaraya.anaraya.screens.services.store.service.my_items

import androidx.paging.PagingData
import com.anaraya.domain.entity.ServiceStoreItemList
import kotlinx.coroutines.flow.Flow

data class MyItemsServicesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val requestToDeleteMsg: String? = null,
    val itemsServices: Flow<PagingData<ProductStoreItemServiceState>>? = null
)

data class ProductStoreItemServiceState(
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
    val customerInformation: List<ServiceCustomerInformationUiState>,
    val customerWantsToRent: List<Int>,
)

data class ServiceCustomerInformationUiState(
    val listeningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrId: String,
    val phoneNumber: String,
    val email: String,
    val sellingStatus: Int,
    val payMethod: Int,
    val rentTo:String,
    val rentFrom:String
)

fun ServiceStoreItemList.toState(): ProductStoreItemServiceState {
    return ProductStoreItemServiceState(
            id = id,
            category = category,
            categoryId = categoryId,
            subCategory = subCategory,
            subCategoryId = subCategoryId,
            title = title,
            serviceDescription = serviceDescription,
            price = price,
            location = location,
            serviceImageUrl = serviceImageUrl,
            userAction = userAction,
            numberOfBuyers = numberOfBuyers,
            customerInformation = customerInformation.map { d ->
                ServiceCustomerInformationUiState(
                    listeningId = d.listiningId,
                    id = d.id,
                    name = d.name,
                    nationalID = d.nationalID,
                    hrId = d.hrid,
                    phoneNumber = d.phoneNumber,
                    email = d.email,
                    payMethod = d.payMethod,
                    sellingStatus = d.rentStatus,
                    rentFrom = d.rentFrom,
                    rentTo = d.rentTo
                )
            },
            customerWantsToRent = customerWantsToRent
        )



}