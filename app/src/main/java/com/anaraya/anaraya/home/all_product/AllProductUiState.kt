package com.anaraya.anaraya.home.all_product

import androidx.paging.PagingData
import com.anaraya.anaraya.home.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow

data class AllProductUiState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val addCartUiState: String? = null,
    val products: Flow<PagingData<ProductUiState>>?=null
)