package com.anaraya.anaraya.screens.services.store.product.my_items.item_sold

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.services.store.product.my_items.toState
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
class ItemSoldViewModel @AssistedInject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @ApplicationContext private val context: Context,
    @Assisted private val productId: Int,
) : ViewModel() {
    private val _product = MutableStateFlow(ItemSoldUiState())
    val product = _product as StateFlow<ItemSoldUiState>

    init {
        getStoreProductByIdForOwner()
    }

    fun getAllData(){
        getStoreProductByIdForOwner()
    }

    private fun getStoreProductByIdForOwner() {
        _product.update {
            ItemSoldUiState(
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
                product = response.toState(false, isListed = false),
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

    @AssistedFactory
    interface ItemSoldAssistedFactory {
        fun create(productId: Int): ItemSoldViewModel
    }

    companion object {
        fun createItemSoldFactory(
            assistedFactory: ItemSoldAssistedFactory,
            productId: Int,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(productId) as T
                }
            }
        }
    }
}