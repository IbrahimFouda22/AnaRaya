package com.anaraya.anaraya.home_design

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.home.CategoryUiState
import com.anaraya.anaraya.screens.home.ProductAdUiState
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow


data class HomeDesignUiState(
    val isLoading: Boolean = false,
    val navigateToAuth: Boolean = false,
    val visibilityTrending: Boolean = false,
    val error: String? = null,
    val productsAdsUiState: List<ProductAdUiState> = emptyList(),
    val productUiState: Flow<PagingData<ProductUiState>>? = null,
    val categoryUiState: Flow<PagingData<CategoryUiState>>? = null,
)
