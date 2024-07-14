package com.anaraya.anaraya.screens.trending_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.screens.category_product.toUiState
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.Product
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProductViewModel @Inject constructor(
    private val manageProductUseCase: ManageProductUseCase,
    private val manageCartUseCase: ManageCartUseCase
) : ViewModel() {

    private val _products = MutableStateFlow(AllProductUiState())
    val products = _products as StateFlow<AllProductUiState>


    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

//    private val _addToCartState = MutableStateFlow(AllProductUiState())
//    val addToCartState = _addToCartState as StateFlow<AllProductUiState>

    init {
        getAllProduct()
    }

    fun getAllData() {
        getAllProduct()
    }

    private fun getAllProduct() {
        showLoading(true)
        _products.update {
            AllProductUiState(
                isLoading = true,
                error = null,
                products = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAllProductSuccess(manageProductUseCase.getTrendingProducts().cachedIn(viewModelScope))
            } catch (e: NoInternetException) {
                onGetAllProductFailure("No Internet")
            } catch (e: Exception) {
                onGetAllProductFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllProductFailure(error: String) {
        showLoading(false)
        _products.update {
            it.copy(
                isLoading = false,
                error = error,
                products = null
            )
        }
    }

    private fun onGetAllProductSuccess(allProduct: Flow<PagingData<Product>>) {
        showLoading(false)
        _products.update {
            it.copy(
                isLoading = false,
                error = null,
                products = allProduct.map {
                    it.map { data ->
                        data.toUiState()
                    }
                }
            )
        }
    }

    fun addToCart(productId: Int, productQty: Int) {
        //showLoading(true)
        _products.update {
            it.copy(
                isLoading = true,
                error = null,
                addCartUiState = null,
                isSucceedAddCartUiState = false
            )
        }
        viewModelScope.launch {
            try {
                onAddToCartSuccess(manageCartUseCase.addCart(productId, productQty))
            } catch (e: NoInternetException) {
                onAddToCartFailure("No Internet")
            } catch (e: Exception) {
                onAddToCartFailure(e.message.toString())
            }
        }
    }

    private fun onAddToCartSuccess(response: AddRemoveCart) {
       // showLoading(false)
        _products.update {
            it.copy(
                isLoading = false,
                error = null,
                addCartUiState = response.message,
                isSucceedAddCartUiState = response.isSucceed
            )
        }
        if(response.isSucceed)
            getAllProduct()
    }

    private fun onAddToCartFailure(error: String) {
       // showLoading(false)
        _products.update {
            it.copy(
                isLoading = false,
                error = error,
                addCartUiState = null,
                isSucceedAddCartUiState = false
            )
        }
    }

    fun setError(error: String?) {
        _products.update {
            it.copy(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun showLoading(boolean: Boolean){
        _loadingState.update {
            boolean
        }
    }

}