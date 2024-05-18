package com.anaraya.anaraya.home.services.store.my_items

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.ProductStoreItemList
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
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _myItemsUiState = MutableStateFlow(MyItemsUiState())
    val myItemsUiState = _myItemsUiState as StateFlow<MyItemsUiState>

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    init {
        // getItemsServices()
    }

    fun getAllData(statusId: Int,isProducts: Boolean) {
        getItemsServices(statusId,isProducts)
    }

    fun getItemsServices(statusId: Int,isProducts: Boolean) {
        _myItemsUiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetItemsServicesSuccess(manageStoreUseCase.getStoreProductAndService(statusId,isProducts))
            } catch (e: NoInternetException) {
                onGetItemsServicesFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetItemsServicesFailure(e.message.toString())
            }
        }
    }

    private fun onGetItemsServicesSuccess(response: Flow<PagingData<ProductStoreItemList>>) {
        _myItemsUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                itemsServices = response.map { d ->
                    d.map { data ->
                        data.toState()
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

    fun requestToDelete(customerProductId: Int,isProducts: Boolean) {
        _myItemsUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                requestToDeleteMsg = null
            )
        }
        viewModelScope.launch {
            try {
                onRequestToDeleteSuccess(manageStoreUseCase.requestToDelete(customerProductId,isProducts))
            } catch (e: NoInternetException) {
                onRequestToDeleteFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onRequestToDeleteFailure(e.message.toString())
            }
        }
    }

    private fun onRequestToDeleteSuccess(response: BaseResponse) {
        _myItemsUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                requestToDeleteMsg = response.message
            )
        }
    }

    private fun onRequestToDeleteFailure(error: String) {
        _myItemsUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                requestToDeleteMsg = null
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
    fun resetMsg(){
        _myItemsUiState.update {
            it.copy(
                requestToDeleteMsg = null
            )
        }
    }

}