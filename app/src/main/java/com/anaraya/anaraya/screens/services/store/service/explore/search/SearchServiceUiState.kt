package com.anaraya.anaraya.screens.services.store.service.explore.search

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.services.store.product.explore.explore_products.CategoryUiState
import com.anaraya.anaraya.screens.services.store.service.explore.explore_services.ExploreServiceUiDetails
import kotlinx.coroutines.flow.Flow

data class SearchServiceUiState(
    val isLoading: Boolean = false,
    val navigateToProductDetails: Boolean = false,
    val error: String? = null,
    val products: Flow<PagingData<ExploreServiceUiDetails>>? = null,
    val subCategories: List<CategoryUiState> = arrayListOf(),
)

