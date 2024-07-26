package com.anaraya.anaraya.screens.services.store.service.explore.service_details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.ServiceStoreForCustomer
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

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("UNCHECKED_CAST")
class ExploreServiceDetailsViewModel @AssistedInject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @Assisted private val serviceId: Int,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _product = MutableStateFlow(ExploreServiceDetailsUiState())
    val product = _product as StateFlow<ExploreServiceDetailsUiState>

    init {
        getStoreServiceByIdForCustomer()
    }
    fun getStoreServiceByIdForCustomer() {
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
                onGetServiceSuccess(manageStoreUseCase.getStoreServiceByIdForCustomer(serviceId = serviceId))
            } catch (e: NoInternetException) {
                onGetServiceFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetServiceFailure(e.message.toString())
            }
        }
    }
    private fun onGetServiceSuccess(response: ServiceStoreForCustomer) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                product = response.toUiState(),
                visibilityPeriodRent = response.itIsARent && !response.isUserService,
                visibilityButton = !response.isUserService,
                visibilitySellerInfo = response.sellerName.isNotBlank() && !response.isUserService && response.rentStatus >= 1
            )
        }
    }
    private fun onGetServiceFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, product = null
            )
        }
    }
    fun requestToRent(serviceId: Int, from: String?, to: String?) {
        _product.update {
            it.copy(
                isLoading = true,
                requestToBuyMessage = "",
                isSucceedRequestToBuy = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onRequestToRentSuccess(manageStoreUseCase.requestToRent(serviceId = serviceId, rentFrom =  from, rentTo =  to))
            } catch (e: NoInternetException) {
                onRequestToRentFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onRequestToRentFailure(e.message.toString())
            }
        }
    }
    private fun onRequestToRentFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error
            )
        }
    }
    private fun onRequestToRentSuccess(response: BaseResponse) {
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
    interface ItemDetailsServiceAssistedFactory {
        fun create(serviceId: Int): ExploreServiceDetailsViewModel
    }
    companion object {
        fun createItemDetailsServiceFactory(
            assistedFactory: ItemDetailsServiceAssistedFactory,
            serviceId: Int,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(serviceId) as T
                }
            }
        }
    }
}