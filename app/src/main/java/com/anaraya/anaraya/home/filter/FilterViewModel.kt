package com.anaraya.anaraya.home.filter

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.home.category_product.CategoryUiState
import com.anaraya.domain.entity.Category
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageCategoryUseCase: ManageCategoryUseCase
) : ViewModel() {

    private val _filterUiState = MutableStateFlow(FilterUiState())
    val filterUiState = _filterUiState.asStateFlow()

    init {
        getAllCats()
        getAllBrands()
    }

    fun getAllData() {
        getAllCats()
        getAllBrands()
    }

    private fun getAllCats() {
        _filterUiState.update {
            FilterUiState(isLoading = true, error = null)
        }
        try {
            viewModelScope.launch {
                onGetAllCatsSuccess(manageCategoryUseCase.getAllCats())
            }
        } catch (e: NoInternetException) {
            onGetAllCatsFailure(context.getString(R.string.no_internet))
        } catch (e: Exception) {
            onGetAllCatsFailure(e.message.toString())
        }
    }

    private fun onGetAllCatsSuccess(allCats: List<Category>) {
        if (allCats.isNotEmpty()) {
            _filterUiState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    showCat = true,
                    listCatsState = allCats.map {data->
                        CategoryUiState(data.id, data.name)
                    })
            }
        }
        else{
            _filterUiState.update {
                it.copy(isLoading = false, error = null,showCat = false, listCatsState = emptyList())
            }
        }
    }

    private fun onGetAllCatsFailure(error: String?) {
        _filterUiState.update {
            it.copy(isLoading = false, error = error,showCat = false, listCatsState = emptyList())
        }
    }

    private fun getAllBrands() {
        _filterUiState.update {
            FilterUiState(isLoading = true, error = null)
        }
        try {
            viewModelScope.launch {
                onGetAllBrandsSuccess(manageCategoryUseCase.getAllBrands())
            }
        } catch (e: NoInternetException) {
            onGetAllBrandsFailure(context.getString(R.string.no_internet))
        } catch (e: Exception) {
            onGetAllBrandsFailure(e.message.toString())
        }
    }

    private fun onGetAllBrandsSuccess(allCats: List<Category>) {
        if (allCats.isNotEmpty()) {
            _filterUiState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    showBrand = true,
                    listBrandsState = allCats.map {data->
                        CategoryUiState(data.id, data.name)
                    })
            }
        }
        else{
            _filterUiState.update {
                it.copy(isLoading = false, error = null,showBrand = false, listBrandsState = emptyList())
            }
        }
    }

    private fun onGetAllBrandsFailure(error: String?) {
        _filterUiState.update {
            it.copy(isLoading = false, error = error,showBrand = false, listBrandsState = emptyList())
        }
    }

}