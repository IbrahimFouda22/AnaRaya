package com.anaraya.anaraya.screens.filter

import com.anaraya.anaraya.screens.category_product.CategoryUiState

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
