package com.anaraya.anaraya.screens.services.store.product.explore.search

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.services.store.product.explore.explore_products.CategoryUiState
import com.anaraya.anaraya.screens.services.store.product.explore.explore_products.ExploreProductUiDetails
import kotlinx.coroutines.flow.Flow

data class SearchProductUiState(
    val isLoading: Boolean = false,
    val navigateToProductDetails: Boolean = false,
    val error: String? = null,
    val products: Flow<PagingData<ExploreProductUiDetails>>? = null,
    val subCategories: List<CategoryUiState> = arrayListOf(),
)

