package com.anaraya.anaraya.screens.services.store.product.explore

import com.anaraya.anaraya.screens.home.CategoryUiState

data class ExploreUiState(
    val isLoading:Boolean = false,
    val error:String?=null,
    val exploreCategoriesList: List<CategoryUiState> = emptyList()
)
