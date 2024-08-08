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
        availableFromDate = if (rentedPeriods.isEmpty()) {
            if (baseRentFrom.isNotEmpty()) baseRentFrom.formatDate(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            ) else null
        } else {
            rentedPeriods.mapNotNull { period ->
                try {
                    period.rentTo.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                } catch (e: Exception) {
                    null
                }
            }.maxOrNull()
        },
        availableToDate = if (baseRentTo.isNotEmpty()) baseRentTo.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") else null,
        baseRentFrom = if (baseRentFrom.isNotEmpty()) baseRentFrom.formatDate("MMM dd, yyyy hh:mm a") else "",
        baseRentTo = if (baseRentTo.isNotEmpty()) baseRentTo.formatDate("MMM dd, yyyy hh:mm a") else "",
        rentFrom = if (rentFrom.isNotEmpty()) rentFrom.formatDate("YYYY-MM-dd hh:mm a") else "",
        rentTo = if (rentTo.isNotEmpty()) rentTo.formatDate("YYYY-MM-dd hh:mm a") else "",
        numOfRented = rentedPeriods.size
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
    val numOfRented: Int,
    val isUserService: Boolean,
    val itIsARent: Boolean,
    val availableFromDate: String? = null,
    val availableToDate: String? = null,
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