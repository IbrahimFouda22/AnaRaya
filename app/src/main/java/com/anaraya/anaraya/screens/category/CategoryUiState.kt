package com.anaraya.anaraya.screens.category

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.home.CategoryUiState
import kotlinx.coroutines.flow.Flow


data class CategoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    //val categoryUiStateData: List<CategoryUiStateData> = arrayListOf()
    val categoryUiStateData: Flow<PagingData<CategoryUiState>>? = null,
    val navigateToSearch: Boolean = false
)


//data class CategoryUiStateData(
//    val id: Int,
//    val image: String? = null,
//    val name: String,
//)