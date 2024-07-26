package com.anaraya.anaraya.screens.services.store.service.explore.explore_services

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.Category
import com.anaraya.domain.entity.ExploreServices
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ExploreCategoriesServiceViewModel @AssistedInject constructor(
    @Assisted val categoryId: Int,
    @Assisted val categoryName: String,
    private val manageStoreUseCase: ManageStoreUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _products = MutableStateFlow(ExploreServiceUiState())
    val products = _products as StateFlow<ExploreServiceUiState>

    val catName = MutableStateFlow("")

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    private val _navigateToCart = MutableStateFlow(false)
    val navigateToCart = _navigateToCart as StateFlow<Boolean>


    private val _categories = MutableStateFlow(ExploreServiceUiState())
    val categories = _categories as StateFlow<ExploreServiceUiState>

    init {
        getAllSubCat()
    }

    fun getAllData(catId: Int) {
        getAllSubCat()
        when (catId) {
            -1 -> {
                getAllProduct()
            }

            else -> {
                getProductBySubCat(catId)
            }
        }
    }

    fun getProductBySubCat(subCatID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _products.update {
                ExploreServiceUiState(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onGetProductByCatSuccess(
                    manageStoreUseCase.getExploreServices(
                        catId = categoryId,
                        subCatId = subCatID
                    )
                )
            } catch (e: NoInternetException) {
                onProductByCatFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onProductByCatFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductByCatSuccess(productsByCategory: Flow<PagingData<ExploreServices>>) {
        _products.update {
            ExploreServiceUiState(isLoading = false,
                error = null,
                products = productsByCategory.map {
                    it.map { data ->
                        data.toUiState()
                    }
                })
        }
    }

    private fun onProductByCatFailure(error: String) {
        _products.update {
            ExploreServiceUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun getAllProduct() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                onGetAllProductSuccess(manageStoreUseCase.getExploreServices(catId = categoryId))
            } catch (e: NoInternetException) {
                onGetAllProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllProductFailure(e.message.toString())
            }
        }

    }

    private fun onGetAllProductFailure(error: String) {
        _products.update {
            ExploreServiceUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    private fun onGetAllProductSuccess(allProduct: Flow<PagingData<ExploreServices>>) {
        _products.update {
            ExploreServiceUiState(isLoading = false,
                error = null,
                products = allProduct.map {
                    it.map { data ->
                        data.toUiState()
                    }
                }
            )
        }
    }

    fun setCatName(name: String) {
        catName.update { name }
    }

    private fun getAllSubCat() {
        showLoading(true)
        viewModelScope.launch {
            _categories.update {
                ExploreServiceUiState(
                    isLoading = true, error = null
                )
            }
            try {
                onGetAllCatSuccess(manageStoreUseCase.getStoreSubCategory(categoryId))
            } catch (e: NoInternetException) {
                onGetAllCatFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllCatFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllCatSuccess(allCategory: List<Category>) {
        showLoading(false)
        _categories.update {
            ExploreServiceUiState(isLoading = false, error = null, subCategories = allCategory.map {
                CategoryUiState(
                    it.id, it.name
                )
            })
        }
    }


    private fun onGetAllCatFailure(error: String) {
        showLoading(false)
        _categories.update {
            ExploreServiceUiState(
                isLoading = false,
                error = error,
            )
        }
    }


    fun setError(error: String?) {
        _products.update {
            ExploreServiceUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(
            categoryId: Int,
            categoryName: String,
        ): ExploreCategoriesServiceViewModel
    }

    companion object {
        fun createProvider(
            categoryId: Int, categoryName: String, factory: AssistedFactory,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(categoryId, categoryName) as T
                }
            }
        }
    }
}