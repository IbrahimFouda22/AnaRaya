package com.anaraya.anaraya.screens.category_product

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.Category
import com.anaraya.domain.entity.Product
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageCategoryUseCase
import com.anaraya.domain.usecase.ManageProductUseCase
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
class CategoryProductViewModel @AssistedInject constructor(
    @Assisted val categoryId: Int,
    @Assisted val categoryName: String,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageCategoryUseCase: ManageCategoryUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _products = MutableStateFlow(CategoryProductUiState())
    val products = _products as StateFlow<CategoryProductUiState>

    val catName = MutableStateFlow(categoryName)

    private val _addCartState = MutableStateFlow(CategoryProductUiState())
    val addCartState = _addCartState as StateFlow<CategoryProductUiState>

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    private val _navigateToCart = MutableStateFlow(false)
    val navigateToCart = _navigateToCart as StateFlow<Boolean>


    private val _categories = MutableStateFlow(CategoryProductUiState())
    val categories = _categories as StateFlow<CategoryProductUiState>

    init {
        getAllCat()
        getAllProduct()
    }
    fun getAllData(catId: Int) {
        getAllCat()
        when (catId) {
            -1 -> {
                getAllProduct()
            }
            else -> {
                getProductByCat(catId)
            }
        }
    }

    fun getProductByCat(catId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _products.update {
                CategoryProductUiState(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onGetProductByCatSuccess(
                    manageProductUseCase.getProductsByCategory(
                        catId
                    )
                )
            } catch (e: NoInternetException) {
                onProductByCatFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onProductByCatFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductByCatSuccess(productsByCategory: Flow<PagingData<Product>>) {
        _products.update {
            CategoryProductUiState(isLoading = false,
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
            CategoryProductUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun getAllProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetAllProductSuccess(manageProductUseCase.getProductsByMainCategory(categoryId))
            } catch (e: NoInternetException) {
                onGetAllProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllProductFailure(e.message.toString())
            }
        }

    }

    private fun onGetAllProductFailure(error: String) {
        _products.update {
            CategoryProductUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    private fun onGetAllProductSuccess(allProduct: Flow<PagingData<Product>>) {
        _products.update {
            CategoryProductUiState(isLoading = false,
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

    fun resetState() {
        _addCartState.update {
            CategoryProductUiState()
        }
    }

    fun setNumOfBasket(num: Int) {
        _products.update {
            it.copy(
                numOfBasket = num
            )
        }
    }

    private fun getAllCat() {
        showLoading(true)
        viewModelScope.launch {
            _categories.update {
                CategoryProductUiState(
                    isLoading = true, error = null
                )
            }
            try {
                onGetAllCatSuccess(manageCategoryUseCase.getAllCatsInsideMainCat(categoryId))
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
            CategoryProductUiState(isLoading = false, error = null, categories = allCategory.map {
                CategoryUiState(
                    it.id, it.name
                )
            })
        }
    }


    private fun onGetAllCatFailure(error: String) {
        showLoading(false)
        _categories.update {
            CategoryProductUiState(
                isLoading = false,
                error = error,
            )
        }
    }

    fun addToCart(productId: Int, productQty: Int) {
        showLoading(true)
        _addCartState.update {
            it.copy(
                isLoading = true,
                error = null,
                addCartUiState = null,
                addCartSucceed = false
            )
        }
        viewModelScope.launch {
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
        _addCartState.update {
            it.copy(
                isLoading = false,
                error = null,
                addCartUiState = response.message,
                addCartSucceed = response.isSucceed
            )
        }
    }

    private fun onAddToCartFailure(error: String) {
        showLoading(false)
        _addCartState.update {
            it.copy(
                isLoading = false,
                error = error,
                addCartUiState = null,
                addCartSucceed = false
            )
        }
    }


    fun setError(error: String?) {
        _products.update {
            CategoryProductUiState(
                isLoading = false, error = error, products = null
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }

    fun navigateToCart() {
        _navigateToCart.update {
            true
        }
    }

    fun navigateToCartDone() {
        _navigateToCart.update {
            false
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(
            categoryId: Int,
            categoryName: String,
        ): CategoryProductViewModel
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