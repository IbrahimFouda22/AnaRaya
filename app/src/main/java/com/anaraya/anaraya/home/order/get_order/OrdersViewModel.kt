package com.anaraya.anaraya.home.order.get_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.domain.entity.Order
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val manageOrdersUseCase: ManageOrdersUseCase
) : ViewModel() {

    private val _orderUiState = MutableStateFlow(OrdersUiState())
    val orderUiState: StateFlow<OrdersUiState> = _orderUiState


    init {
        getOrders()
    }

    fun getAllData() {
         getOrders()
    }

    private fun getOrders() {
        _orderUiState.update {
            OrdersUiState(
                isLoading = true,
                error = null,
                ordersUiState = emptyList()
            )
        }
        viewModelScope.launch {
            try {
                onGetOrdersSuccess(manageOrdersUseCase.getOrders())
            } catch (e: NoInternetException) {
                onGetOrdersFailure("No Internet")
            } catch (e: Exception) {
                onGetOrdersFailure(e.message.toString())
            }
        }
    }

    private fun onGetOrdersSuccess(order: Order) {
        _orderUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                ordersUiState = order.data.map { data ->
                    data.toUiState()
                }
            )
        }
    }

    private fun onGetOrdersFailure(error: String) {
        _orderUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                ordersUiState = emptyList()
            )
        }
    }
}