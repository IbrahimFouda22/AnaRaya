package com.anaraya.anaraya.screens.services.store.product.explore.product_details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.ProductStoreForCustomer
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
class ExploreProductDetailsViewModel @AssistedInject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @Assisted private val productId: Int,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _product = MutableStateFlow(ExploreProductDetailsUiState())
    val product = _product as StateFlow<ExploreProductDetailsUiState>

    init {
        getStoreProductByIdForOwner()
    }

    fun getStoreProductByIdForOwner() {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                requestToBuyMessage = "",
                isSucceedRequestToBuy = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetProductSuccess(manageStoreUseCase.getStoreProductByIdForCustomer(productId = productId))
            } catch (e: NoInternetException) {
                onGetProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetProductFailure(e.message.toString())
            }
        }
    }
    private fun onGetProductSuccess(response: ProductStoreForCustomer) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                product = response.toUiState(),
                visibilityButton = !response.isUserProduct,
                visibilitySellerInfo = response.sellerName.isNotBlank() && !response.isUserProduct && response.sellingStatus >= 1 && !response.isAnonymous
            )
        }
    }

    private fun onGetProductFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, product = null
            )
        }
    }

    fun requestToBuy(productId: Int) {
        _product.update {
            it.copy(
                isLoading = true,
                requestToBuyMessage = "",
                isSucceedRequestToBuy = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onRequestToBuySuccess(manageStoreUseCase.requestToBuy(productId))
            } catch (e: NoInternetException) {
                onRequestToBuyFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onRequestToBuyFailure(e.message.toString())
            }
        }

    }

    private fun onRequestToBuyFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error
            )
        }
    }

    private fun onRequestToBuySuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                requestToBuyMessage = response.message,
                isSucceedRequestToBuy = response.isSucceed
            )
        }
    }


    @AssistedFactory
    interface ItemDetailsProductAssistedFactory {
        fun create(productId: Int): ExploreProductDetailsViewModel
    }

    companion object {
        fun createItemDetailsProductFactory(
            assistedFactory: ItemDetailsProductAssistedFactory,
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