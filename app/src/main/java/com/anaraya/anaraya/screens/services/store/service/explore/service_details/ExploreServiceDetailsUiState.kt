package com.anaraya.anaraya.screens.services.store.service.explore.service_details

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.anaraya.domain.entity.ServiceStoreForCustomer
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ExploreServiceDetailsUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val visibilitySellerInfo: Boolean = false,
    val visibilityButton: Boolean = false,
    val visibilityPeriodRent: Boolean = false,
    val isSucceedRequestToBuy: Boolean = false,
    val requestToBuyMessage: String? = null,
    val error: String? = null,
    val product: ExploreServiceDetailsUiDetails? = null,
)

@RequiresApi(Build.VERSION_CODES.O)
fun ServiceStoreForCustomer.toUiState(): ExploreServiceDetailsUiDetails {
    return ExploreServiceDetailsUiDetails(
        id = id,
        category = category,
        subCategory = subCategory,
        title = title,
        serviceDescription = serviceDescription,
        price = price,
        location = location,
        serviceImageUrl = serviceImageUrl,
        sellerName = sellerName,
        rentStatus = rentStatus,
        sellerPhoneNumber = sellerPhoneNumber,
        isUserService = isUserService,
        itIsARent = itIsARent,
        baseRentFrom = if (baseRentFrom.isNotEmpty()) baseRentFrom.formatDate("MMM dd, yyyy") else "",
        baseRentTo = if (baseRentTo.isNotEmpty()) baseRentTo.formatDate("MMM dd, yyyy") else "",
        rentFrom = if (rentFrom.isNotEmpty()) rentFrom.formatDate("YYYY-MM-dd") else "",
        rentTo = if (rentTo.isNotEmpty()) rentTo.formatDate("YYYY-MM-dd") else ""
    )
}

data class ExploreServiceDetailsUiDetails(
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
    val isUserService: Boolean,
    val itIsARent: Boolean,
) : Serializable

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun String.formatDate(format: String): String {
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern(format)

    val dateTime = LocalDateTime.parse(this, inputFormatter)
    val formattedDate = dateTime.format(outputFormatter)
    return formattedDate
}