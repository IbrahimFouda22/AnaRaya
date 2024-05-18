package com.anaraya.anaraya.home.services.store.my_items

import androidx.paging.PagingData
import com.anaraya.domain.entity.ProductStoreItemList
import kotlinx.coroutines.flow.Flow

data class MyItemsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val requestToDeleteMsg: String? = null,
    val itemsServices: Flow<PagingData<ProductStoreItemState>>? = null
)

data class ProductStoreItemState(
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
    val numberOfBuyers: Int,
    val customerInformation: List<CustomerInformationState>,
    val customerWantsToBuy: List<Int>,
)

data class CustomerInformationState(
    val listeningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrId: String,
    val phoneNumber: String,
    val email: String,
    val sellingStatus: Int,
    val payMethod: Int
)

fun ProductStoreItemList.toState(): ProductStoreItemState {
    return ProductStoreItemState(
            id = id,
            category = category,
            categoryId = categoryId,
            subCategory = subCategory,
            subCategoryId = subCategoryId,
            condition = condition,
            title = title,
            itemDescription = itemDescription,
            price = price,
            location = location,
            productImageUrl = productImageUrl,
            userAction = userAction,
            numberOfBuyers = numberOfBuyers,
            customerInformation = customerInformation.map { d ->
                CustomerInformationState(
                    listeningId = d.listeningId,
                    id = d.id,
                    name = d.name,
                    nationalID = d.nationalID,
                    hrId = d.hrId,
                    phoneNumber = d.phoneNumber,
                    email = d.email,
                    payMethod = d.payMethod,
                    sellingStatus = d.sellingStatus
                )
            },
            customerWantsToBuy = customerWantsToBuy
        )



}