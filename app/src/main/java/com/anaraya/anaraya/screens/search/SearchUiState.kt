package com.anaraya.anaraya.screens.search

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: Flow<PagingData<ProductUiState>>? = null,
    val navigateToFilter: Boolean = false,
    val addCartUiState: String? = null
)