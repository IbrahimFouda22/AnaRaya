package com.anaraya.anaraya.screens.trending_products

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow

data class AllProductUiState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val addCartUiState: String? = null,
    val isSucceedAddCartUiState: Boolean = false,
    val products: Flow<PagingData<ProductUiState>>?=null
)