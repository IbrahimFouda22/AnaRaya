package com.anaraya.anaraya.screens.services.store.product.my_items

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.ProductStore
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyItemsViewModel @Inject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) :
    ViewModel() {

    private val _myItemsUiState = MutableStateFlow(MyItemsUiState())
    val myItemsUiState = _myItemsUiState as StateFlow<MyItemsUiState>

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>


    fun getAllData(status: Int) {
        getItemsServices(status)
    }

    fun getItemsServices(status: Int) {
        _myItemsUiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetItemsServicesSuccess(manageStoreUseCase.getStoreProduct(status), status)
            } catch (e: NoInternetException) {
                onGetItemsServicesFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetItemsServicesFailure(e.message.toString())
            }
        }
    }

    private fun onGetItemsServicesSuccess(response: Flow<PagingData<ProductStore>>, status: Int) {
        _myItemsUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                itemsServices = response.map { d ->
                    d.map { data ->
                        data.toState(
                            visibilityBadge = status == 1 && data.customerWantsToBuy > 0,
                            isListed = status == 1
                        )
                    }
                }
            )
        }
    }

    private fun onGetItemsServicesFailure(error: String) {
        _myItemsUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                itemsServices = null
            )
        }
    }


    fun setError(error: String?) {
        _myItemsUiState.update {
            MyItemsUiState(
                isLoading = false, error = error, itemsServices = null
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }

    fun resetMsg() {
        _myItemsUiState.update {
            it.copy(
                requestToDeleteMsg = null
            )
        }
    }

}