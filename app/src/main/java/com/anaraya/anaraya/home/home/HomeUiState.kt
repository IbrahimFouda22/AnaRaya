package com.anaraya.anaraya.home.home

import androidx.paging.PagingData
import com.anaraya.anaraya.home.shared_data.Image
import com.anaraya.anaraya.home.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow


data class HomeUiState(
    val isLoading: Boolean = false,
    val navigateToSchedule: Boolean = false,
    val navigateToSearch: Boolean = false,
    val navigateToAllProduct: Boolean = false,
    val visibilityTrending: Boolean = false,
    val error: String? = null,
    val productsAdsUiState: List<ProductAdUiState> = emptyList(),
    val addCartUiState: String? = null,
    //val productUiState: List<ProductUiState> = arrayListOf(),
    val productUiState: Flow<PagingData<ProductUiState>>? = null,
    val categoryUiState: Flow<PagingData<CategoryUiState>>? = null,
    //val categoryUiState: List<CategoryUiState> = arrayListOf()
)

data class ProductAdUiState(
    override val image: String?, val id: Int, val isProductOrBrand: Boolean
) : Image(image)


data class CategoryUiState(
    val id: Int,
    val image: String? = null,
    val name: String,
)