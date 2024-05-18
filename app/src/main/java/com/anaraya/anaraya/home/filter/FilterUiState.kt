package com.anaraya.anaraya.home.filter

import com.anaraya.anaraya.home.category_product.CategoryUiState

data class FilterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val showCat: Boolean = false,
    val showBrand: Boolean = false,
    //val highest: Boolean = false,
    //val lowest: Boolean = false,
    val listCatsState: List<CategoryUiState> = emptyList(),
    val listBrandsState: List<CategoryUiState> = emptyList(),
)
