package com.anaraya.anaraya.screens.points_products

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow

data class PointsProductsUiState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val addCartUiState: String? = null,
    val isSucceedAddCartUiState: Boolean = false,
    val products: Flow<PagingData<ProductUiState>>?=null
)