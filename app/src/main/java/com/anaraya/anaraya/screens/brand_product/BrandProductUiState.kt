package com.anaraya.anaraya.screens.brand_product

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow

data class CategoryProductUiState(
    val isLoading: Boolean = false,
    val navigateToCart: Boolean = false,
    val error: String? = null,
    //val products: List<ProductUiState> = arrayListOf(),
    val products: Flow<PagingData<ProductUiState>>?=null,
    val addCartUiState: String? = null,
    //val updateProducts: Flow<PagingData<ProductUiState>>?=null,
)



