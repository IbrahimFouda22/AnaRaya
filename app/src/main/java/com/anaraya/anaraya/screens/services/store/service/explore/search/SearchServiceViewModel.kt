package com.anaraya.anaraya.screens.services.store.service.explore.search

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.services.store.service.explore.explore_services.toUiState
import com.anaraya.domain.entity.ExploreServices
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchServiceViewModel @Inject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _products = MutableStateFlow(SearchServiceUiState())
    val products = _products as StateFlow<SearchServiceUiState>

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    fun getAllProduct(searchWord: String, searchLanguage: String, catId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetAllProductSuccess(
                    manageStoreUseCase.getExploreServices(
                        searchWord = searchWord,
                        searchLanguage = searchLanguage,
                        catId = catId
                    )
                )
            } catch (e: NoInternetException) {
                onGetAllProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllProductFailure(e.message.toString())
            }
        }

    }

    private fun onGetAllProductFailure(error: String) {
        _products.update {
            SearchServiceUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    private fun onGetAllProductSuccess(allProduct: Flow<PagingData<ExploreServices>>) {
        _products.update {
            SearchServiceUiState(isLoading = false,
                error = null,
                products = allProduct.map {
                    it.map { data ->
                        data.toUiState()
                    }
                }
            )
        }
    }

    fun setError(error: String?) {
        _products.update {
            SearchServiceUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }
}