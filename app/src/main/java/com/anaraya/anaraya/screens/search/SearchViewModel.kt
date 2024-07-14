package com.anaraya.anaraya.screens.search

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.category_product.toUiState
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.Product
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageProductUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class SearchViewModel @AssistedInject constructor(
    @Assisted val id: String,
    @Assisted val selectionType: Int,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) :
    ViewModel() {

    private val _products = MutableStateFlow(SearchUiState())
    val products = _products as StateFlow<SearchUiState>

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    init {
        if (selectionType != -1) {
            getProductByCat(id.toInt())
        }
    }

    fun getAllData(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
    ) {
        search(searchWord, searchLanguage, catIds, brandIds, highestOrLowest)
    }

    fun search(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
    ) {
        _products.update {
            SearchUiState(
                isLoading = true,
                error = null,
                products = null
            )
        }
        showLoading(true)
        viewModelScope.launch (Dispatchers.IO){
            try {
                delay(200)
                onGetProductsSuccess(
                    manageProductUseCase.search(
                        searchWord,
                        searchLanguage,
                        catIds,
                        brandIds,
                        highestOrLowest
                    )
                )
            } catch (e: NoInternetException) {
                onGetProductsFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetProductsFailure(e.message.toString())
            }
        }
    }


    private fun onGetProductsSuccess(products: Flow<PagingData<Product>>) {
        showLoading(false)
        _products.update {
            SearchUiState(
                isLoading = false,
                error = null,
                products = products.map {
                    it.map { data ->
                        data.toUiState()
                    }
                }
            )
        }
    }


    private fun onGetProductsFailure(error: String) {
        showLoading(false)
        _products.update {
            SearchUiState(
                isLoading = false,
                error = error
            )
        }
    }

    private fun getProductByCat(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _products.update {
                SearchUiState(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                when (selectionType) {
                    2 -> {
                        onGetProductByCatSuccess(
                            manageProductUseCase.getProductsByMainCategory(
                                categoryId
                            )
                        )
                    }

                    3 -> {
                        onGetProductByCatSuccess(
                            manageProductUseCase.getProductsByCategory(
                                categoryId
                            )
                        )
                    }

                    4 -> {
                        onGetProductByCatSuccess(
                            manageProductUseCase.getProductsByBrand(
                                categoryId
                            )
                        )
                    }
                }
            } catch (e: NoInternetException) {
                onProductByCatFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onProductByCatFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductByCatSuccess(productsByCategory: Flow<PagingData<Product>>) {
        _products.update {
            SearchUiState(isLoading = false,
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
            SearchUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun navigateToFilter() {
        _products.update {
            SearchUiState(
                navigateToFilter = true
            )
        }
    }

    fun navigateToFilterDone() {
        _products.update {
            SearchUiState(
                navigateToFilter = false
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }

    fun setError(error: String?) {
        _products.update {
            SearchUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun addToCart(productId: Int, productQty: Int) {
        showLoading(true)
        _products.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onAddToCartSuccess(manageCartUseCase.addCart(productId, productQty))
            } catch (e: NoInternetException) {
                onAddToCartFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddToCartFailure(e.message.toString())
            }
        }
    }

    private fun onAddToCartSuccess(response: AddRemoveCart) {
        showLoading(false)
        _products.update {
            it.copy(
                isLoading = false,
                error = null,
                addCartUiState = response.message
            )
        }
    }

    private fun onAddToCartFailure(error: String) {
        showLoading(false)
        _products.update {
            it.copy(
                isLoading = false,
                error = error,
                addCartUiState = null
            )
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(
            id: String,
            selectionType: Int,
        ): SearchViewModel
    }

    companion object {
        fun createProvider(
            id: String, selectionType: Int, factory: AssistedFactory,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(id, selectionType) as T
                }
            }
        }
    }
}