package com.anaraya.anaraya.screens.services.store.product.my_items.item_details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.services.store.product.my_items.toState
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.ProductStore
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ItemDetailsProductViewModel @AssistedInject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @ApplicationContext private val context: Context,
    @Assisted private val productId: Int
) : ViewModel() {
    private val _product = MutableStateFlow(ItemDetailsProductUiState())
    val product = _product as StateFlow<ItemDetailsProductUiState>

    init {
        getStoreProductByIdForOwner()
    }
    private fun getStoreProductByIdForOwner() {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetProductSuccess(manageStoreUseCase.getStoreProductByIdForOwner(productId = productId))
            } catch (e: NoInternetException) {
                onGetProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetProductFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductSuccess(response: ProductStore) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                product = response.toState(false, isListed = false)
            )
        }
    }

    private fun onGetProductFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                product = null
            )
        }
    }
    fun requestToDelete(customerProductId: Int) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                requestToDeleteMsg = null
            )
        }
        viewModelScope.launch {
            try {
                onRequestToDeleteSuccess(manageStoreUseCase.requestToDeleteProduct(customerProductId))
            } catch (e: NoInternetException) {
                onRequestToDeleteFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onRequestToDeleteFailure(e.message.toString())
            }
        }
    }

    private fun onRequestToDeleteSuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                requestToDeleteMsg = response.message,
                isSucceedRequestToDelete = response.isSucceed
            )
        }
    }

    private fun onRequestToDeleteFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                requestToDeleteMsg = null,
                isSucceedRequestToDelete = false
            )
        }
    }

    fun resetMsg() {
        _product.update {
            it.copy(
                requestToDeleteMsg = null
            )
        }
    }
    @AssistedFactory
    interface ItemDetailsProductAssistedFactory {
        fun create(productId: Int): ItemDetailsProductViewModel
    }

    companion object {
        fun createItemDetailsProductFactory(
            assistedFactory: ItemDetailsProductAssistedFactory,
            productId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(productId) as T
                }
            }
        }
    }
}