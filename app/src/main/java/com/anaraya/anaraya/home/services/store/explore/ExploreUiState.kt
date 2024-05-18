package com.anaraya.anaraya.home.services.store.explore

import com.anaraya.anaraya.home.home.CategoryUiState

data class ExploreUiState(
    val isLoading:Boolean = false,
    val error:String?=null,
    val exploreCategoriesList: List<CategoryUiState> = emptyList()
)
