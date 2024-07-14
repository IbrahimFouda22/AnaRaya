package com.anaraya.anaraya.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.domain.entity.MainCategory
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val manageCategoryUseCase: ManageCategoryUseCase,
) : ViewModel() {


    private val _categories = MutableStateFlow(CategoryUiState())
    val categories = _categories as StateFlow<CategoryUiState>

    init {
        getCategory()
    }

    fun getAllDate() {
        getCategory()
    }

    private fun getCategory() {
        viewModelScope.launch {
            _categories.update {
                CategoryUiState(
                    isLoading = true,
                    error = null
                )
            }
            try {
                onGetCategorySuccess(
                    manageCategoryUseCase.getHomeCategory().cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetCategoryFailure("No Internet")
            } catch (e: Exception) {
                onGetCategoryFailure(e.message.toString())
            }
        }
    }

    private fun onGetCategorySuccess(list: Flow<PagingData<MainCategory>>) {
        _categories.update {
            CategoryUiState(
                isLoading = false,
                error = null,
                categoryUiStateData = list.map {
                    it.map { m ->
                        com.anaraya.anaraya.screens.home.CategoryUiState(
                            image = m.image,
                            name = m.name,
                            id = m.id
                        )
                    }
                }
            )
        }
    }

    private fun onGetCategoryFailure(error: String) {
        _categories.update {
            CategoryUiState(
                isLoading = false,
                error = error,
                categoryUiStateData = null
            )
        }
    }

    fun navigateToSearch() {
        _categories.update {
            it.copy(navigateToSearch = true)
        }
    }
    fun navigateToSearchDone() {
        _categories.update {
            it.copy(navigateToSearch = false)
        }
    }
}