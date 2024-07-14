package com.anaraya.anaraya.screens.services.store.service.explore_services

import androidx.paging.PagingData
import com.anaraya.domain.entity.ExploreServices
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

data class ExploreServiceUiState(
    val isLoading: Boolean = false,
    val navigateToServiceDetails: Boolean = false,
    val error: String? = null,
    val products: Flow<PagingData<ExploreServiceUiDetails>>? = null,
    val subCategories: List<CategoryUiState> = arrayListOf(),
)

data class CategoryUiState(
    val id: Int,
    val name: String,
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
        rentTo = rentTo,
        rentFrom = rentFrom,
        price = price,
        location = location,
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
):Serializable

