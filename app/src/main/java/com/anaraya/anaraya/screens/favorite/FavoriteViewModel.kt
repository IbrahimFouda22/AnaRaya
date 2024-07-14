package com.anaraya.anaraya.screens.favorite

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.category_product.toUiState
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import com.anaraya.domain.entity.AddAllToBasket
import com.anaraya.domain.entity.AddDeleteFav
import com.anaraya.domain.entity.Product
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    private val manageFavUseCase: ManageFavUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _products = MutableStateFlow(FavoriteUiState())
    val products = _products as StateFlow<FavoriteUiState>

    init {
        getAllFavProduct()
        //getProductByMainCategory(categoryId)
    }

    fun getAllData() {
        getAllFavProduct()
    }

    private fun getAllFavProduct() {
        viewModelScope.launch {
            _products.update {
                FavoriteUiState(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onGetAllFavProductSuccess(
                    manageFavUseCase.getAllFavorite()
                )
            } catch (e: NoInternetException) {
                onGetAllFavProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllFavProductFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllFavProductSuccess(products: List<Product>) {
        if (products.isEmpty()) {
            _products.update {
                FavoriteUiState(
                    isLoading = false,
                    error = null,
                    showEmptyFav = true,
                    products = emptyList()
                )
            }
        } else {
            _products.update {
                FavoriteUiState(isLoading = false,
                    error = null,
                    showEmptyFav = false,
                    products = products.map {
                        it.toUiState()
                    })
            }
        }
    }

    private fun onGetAllFavProductFailure(error: String) {
        _products.update {
            FavoriteUiState(
                isLoading = false, error = error
            )
        }
    }

    fun deleteFavProduct(productId: Int, position: Int, list: MutableList<ProductUiState>) {
        viewModelScope.launch {
            _products.update {
                FavoriteUiState(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onDeleteFavProductSuccess(
                    manageFavUseCase.deleteFromFav(productId), position, list
                )
            } catch (e: NoInternetException) {
                onDeleteFavProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onDeleteFavProductFailure(e.message.toString())
            }
        }
    }

    private fun onDeleteFavProductSuccess(
        response: AddDeleteFav,
        position: Int,
        list: MutableList<ProductUiState>
    ) {
        list.removeAt(position)
        if (list.isEmpty()) {
            _products.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    showEmptyFav = true,
                    deleteMsg = response.message,
                    products = list.toList()
                )
            }
        } else {
            _products.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    showEmptyFav = false,
                    deleteMsg = response.message,
                    products = list.toList()
                )
            }
        }
        //getAllData()
    }

    private fun onDeleteFavProductFailure(error: String) {
        _products.update {
            it.copy(
                isLoading = false,
                error = error,
                deleteMsg = null
            )
        }
    }

    fun addAllToBasket() {
        viewModelScope.launch {
            _products.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            try {
                onAddAllToBasketSuccess(
                    manageCartUseCase.addAllToBasket()
                )
            } catch (e: NoInternetException) {
                onAddAllToBasketFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddAllToBasketFailure(e.message.toString())
            }
        }
    }

    private fun onAddAllToBasketSuccess(addAllToBasket: AddAllToBasket) {
        _products.update {
            it.copy(
                isLoading = false,
                error = null,
                addAllToBasketMsg = addAllToBasket.message,
                numAdded = addAllToBasket.data,
            )
        }

    }

    private fun onAddAllToBasketFailure(error: String) {
        _products.update {
            it.copy(
                isLoading = false,
                error = error,
                addAllToBasketMsg = null,
                numAdded = 0
            )
        }
    }

    fun setError(error: String?) {
        _products.update {
            FavoriteUiState(
                isLoading = false, error = error
            )
        }
    }

    fun navigateToHome() {
        _products.update {
            it.copy(navigateToHome = true)
        }
    }

    fun navigateToHomeDone() {
        _products.update {
            it.copy(navigateToHome = false)
        }
    }

    fun clearDeleteMsg() {
        _products.update {
            it.copy(deleteMsg = null)
        }
    }
    fun clearAddAll() {
        _products.update {
            it.copy(addAllToBasketMsg = null, numAdded = 0)
        }
    }

}