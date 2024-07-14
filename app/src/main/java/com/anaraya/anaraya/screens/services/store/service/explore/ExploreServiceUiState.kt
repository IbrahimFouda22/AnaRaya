package com.anaraya.anaraya.screens.services.store.service.explore

import com.anaraya.anaraya.screens.home.CategoryUiState

data class ExploreServiceUiState(
    val isLoading:Boolean = false,
    val error:String?=null,
    val exploreCategoriesList: List<CategoryUiState> = emptyList()
)
