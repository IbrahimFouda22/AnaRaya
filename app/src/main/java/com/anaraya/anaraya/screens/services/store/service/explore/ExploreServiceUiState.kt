package com.anaraya.anaraya.screens.services.store.service.explore

import com.anaraya.anaraya.screens.home.CategoryUiState
import com.anaraya.domain.entity.ExploreProduct
import com.anaraya.domain.entity.ExploreServices
import java.io.Serializable

data class ExploreServiceUiState(
    val isLoading:Boolean = false,
    val error:String?=null,
    val exploreCategoriesList: List<CategoryUiState> = emptyList()
)

fun ExploreServices.toUiState(): ExploreServiceUiDetails {
    return ExploreServiceUiDetails(
        category = category,
        id = id,
        subCategory = subCategory,
        serviceDescription = serviceDescription,
        isUserService = isUserService,
        title = title,
        serviceImageUrl = serviceImageUrl,
        rentStatus = rentStatus,
        price = price,
        location = location,
        rentTo = rentTo,
        rentFrom = rentFrom
    )
}

data class ExploreServiceUiDetails(
    val id: Int,
    val title: String,
    val serviceDescription: String,
    val price: Double,
    val location: String,
    val serviceImageUrl: String,
    val category: String,
    val subCategory: String,
    val rentFrom: String,
    val rentTo: String,
    val rentStatus: Int,
    val isUserService: Boolean,
): Serializable