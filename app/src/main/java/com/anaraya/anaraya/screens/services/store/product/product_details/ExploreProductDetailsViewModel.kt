package com.anaraya.anaraya.screens.services.store.product.product_details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreProductDetailsViewModel @Inject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _product = MutableStateFlow(ExploreProductDetailsUiState())
    val product = _product as StateFlow<ExploreProductDetailsUiState>


    fun getAllData(productId: Int) {
        requestToBuy(productId)
    }

    fun requestToBuy(productId: Int) {
        _product.update {
            ExploreProductDetailsUiState(
                isLoading = true
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

    fun setSellingStatus(status: Int) {
        _product.update {
            it.copy(
                sellingStatus = status,
                requestToBuyMessage = null
            )
        }
    }

    fun setSellerVisibility(boolean: Boolean) {
        _product.update {
            it.copy(
                visibilitySellerInfo = boolean,
                requestToBuyMessage = null
            )
        }
    }
}