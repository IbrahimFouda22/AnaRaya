package com.anaraya.anaraya.screens.services.store.product.my_items

import androidx.paging.PagingData
import com.anaraya.domain.entity.ProductStore
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

data class MyItemsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val requestToDeleteMsg: String? = null,
    val itemsServices: Flow<PagingData<ProductStoreItemState>>? = null,
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
    val status: Int,
    val isAnonymous: Boolean,
    val handleDelivery: Boolean,
    val customerInformation: CustomerInformationState? = null,
    val numberOfBuyers: Int,
    val customerWantsToBuy: Int,
    val visibilityBadge: Boolean = false,
    val isListed: Boolean = false,
) : Serializable

data class CustomerInformationState(
    val listeningId: Int,
    val id: String,
    val name: String,
    val nationalID: String,
    val hrId: String,
    val phoneNumber: String,
    val email: String,
    val sellingStatus: Int,
    val payMethod: Int,
) : Serializable

fun ProductStore.toState(
    visibilityBadge: Boolean,
    isListed: Boolean,
    isEditing: Boolean = false,
): ProductStoreItemState {
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
        productImageUrl = if (isEditing) productImageUrl?.replaceAfter(
            "Raya",
            ""
        ) else productImageUrl,
        userAction = userAction,
        status = status,
        isAnonymous = isAnonymous,
        handleDelivery = handleDelivery,
        visibilityBadge = visibilityBadge,
        isListed = isListed,
        customerInformation = customerInformation?.let {
            CustomerInformationState(
                listeningId = it.listeningId,
                id = it.id,
                name = it.name,
                nationalID = it.nationalID,
                hrId = it.hrId,
                phoneNumber = it.phoneNumber,
                email = it.email,
                payMethod = it.payMethod,
                sellingStatus = it.sellingStatus
            )
        },
        numberOfBuyers = if (customerWantsToBuy > 0) 1 else 0,
        customerWantsToBuy = customerWantsToBuy,
    )
}