package com.anaraya.anaraya.screens.services.store.service.my_items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingData
import com.anaraya.anaraya.screens.services.store.service.explore.service_details.formatDate
import com.anaraya.domain.entity.ServiceStoreItemList
import kotlinx.coroutines.flow.Flow

data class MyItemsServicesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val requestToDeleteMsg: String? = null,
    val itemsServices: Flow<PagingData<ProductStoreItemServiceState>>? = null,
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
    val status: Int,
    val rentFrom: String,
    val rentTo: String,
    val customerInformation: List<ServiceCustomerInformationUiState>,
    val customerWantsToRent: List<Int>,
    val visibilityBadge: Boolean = false,
    val isListed: Boolean = false,
    val itIsARent: Boolean,
    val isPaymentConfirmed: Boolean,
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
    val rentTo: String,
    val rentFrom: String,
    val visibilityRent: Boolean,
) {
    var isSelected = false
}

@RequiresApi(Build.VERSION_CODES.O)
fun ServiceStoreItemList.toState(
    visibilityBadge: Boolean,
    isListed: Boolean,
    isEditing: Boolean = false,
): ProductStoreItemServiceState {
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
        serviceImageUrl = if (isEditing) serviceImageUrl?.replaceAfter(
            "Raya", ""
        ) else serviceImageUrl,
        userAction = userAction,
        numberOfBuyers = customerInformation.count {
            it.rentStatus == 1
        },
        status = status,
        visibilityBadge = visibilityBadge,
        isListed = isListed,
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
                rentFrom = if (d.rentFrom.isNotEmpty()) d.rentFrom.formatDate("YYYY-MM-dd") else "",
                rentTo = if (d.rentTo.isNotEmpty()) d.rentTo.formatDate("YYYY-MM-dd") else "",
                visibilityRent = d.rentFrom.isNotEmpty()
            )
        },
        customerWantsToRent = customerWantsToRent,
        rentFrom = if (rentFrom.isNotEmpty()) rentFrom.formatDate("YYYY-MM-dd hh:mm a") else "",
        rentTo = if (rentTo.isNotEmpty()) rentTo.formatDate("YYYY-MM-dd hh:mm a") else "",
        itIsARent = itIsARent,
        isPaymentConfirmed = isPaymentConfirmed
    )
}