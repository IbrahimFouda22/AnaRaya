package com.anaraya.anaraya.screens.total_cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.screens.shared_data.AddOrderUiState
import com.anaraya.domain.entity.ApplyPromo
import com.anaraya.domain.entity.PlaceOrder
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageOrdersUseCase
import com.anaraya.domain.usecase.ManagePromoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class TotalCostViewModel @AssistedInject constructor(
    private val manageOrdersUseCase: ManageOrdersUseCase,
    private val managePromoUseCase: ManagePromoUseCase,
    @Assisted private val addOrderUiState: AddOrderUiState,
    @Assisted private val totalPoints: Double,
) : ViewModel() {

    private val _totalCostUiState = MutableStateFlow(TotalCostUiState())
    val totalCostUiState = _totalCostUiState as StateFlow<TotalCostUiState>

    init {
        _totalCostUiState.update {
            it.copy(
                addOrderUiState = addOrderUiState,
                totalPoints = totalPoints,
                visibilityPoints = totalPoints > 0
            )
        }
    }

    fun getAllData() {
        //placeOrder()
    }

    fun placeOrder(paymentMethod: String) {
        _totalCostUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onPlaceOrderSuccess(manageOrdersUseCase.placeOrder(paymentMethod))
            } catch (e: NoInternetException) {
                onPlaceOrderFailure("No Internet")
            } catch (e: Exception) {
                onPlaceOrderFailure(e.message.toString())
            }
        }
    }

    private fun onPlaceOrderSuccess(placeOrder: PlaceOrder) {
        _totalCostUiState.update {
            it.copy(
                isLoading = false,
                isSucceed = placeOrder.isSucceed,
                message = placeOrder.message,
                error = null,
            )
        }
    }

    private fun onPlaceOrderFailure(error: String) {
        _totalCostUiState.update {
            it.copy(
                isLoading = false,
                isSucceed = false,
                error = error,
                message = null
            )
        }
    }

    fun applyPromo(promoCode: String) {
        _totalCostUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                message = null,
                isSucceedApplyPromo = false,
                messageApplyPromo = null
            )
        }
        viewModelScope.launch {
            try {
                onApplyPromoSuccess(managePromoUseCase.applyPromo(promoCode))
            } catch (e: NoInternetException) {
                onApplyPromoFailure("No Internet")
            } catch (e: Exception) {
                onApplyPromoFailure(e.message.toString())
            }
        }
    }

    private fun onApplyPromoSuccess(response: ApplyPromo) {
        if (response.isSucceed) {
            _totalCostUiState.update {
                it.copy(
                    isLoading = false,
                    isSucceedApplyPromo = true,
                    messageApplyPromo = response.message,
                    error = null,
                    addOrderUiState = it.addOrderUiState!!.copy(
                        total = response.data.cartTotalAmount,
                        discount = response.data.cartPromoCodeDiscount,
                        amountToTakeDeliveryFree = response.data.cartAmountToTakeFreeDelivery
                    )
                )
            }
        } else {
            _totalCostUiState.update {
                it.copy(
                    isLoading = false,
                    isSucceedApplyPromo = false,
                    messageApplyPromo = response.message,
                    error = null,
                )
            }
        }
    }

    private fun onApplyPromoFailure(error: String) {
        _totalCostUiState.update {
            it.copy(
                isLoading = false,
                isSucceedApplyPromo = false,
                error = error,
                messageApplyPromo = null
            )
        }
    }
    fun removePromo() {
        _totalCostUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSucceedApplyPromo = false,
                messageApplyPromo = null
            )
        }
        viewModelScope.launch {
            try {
                onRemovePromoSuccess(managePromoUseCase.removePromoCode())
            } catch (e: NoInternetException) {
                onRemovePromoFailure("No Internet")
            } catch (e: Exception) {
                onRemovePromoFailure(e.message.toString())
            }
        }
    }

    private fun onRemovePromoSuccess(response: ApplyPromo) {
        if (response.isSucceed) {
            _totalCostUiState.update {
                it.copy(
                    isLoading = false,
                    isSucceedRemovePromo = true,
                    messageRemovePromo = response.message,
                    error = null,
                    addOrderUiState = it.addOrderUiState!!.copy(
                        total = response.data.cartTotalAmount,
                        discount = response.data.cartPromoCodeDiscount,
                        amountToTakeDeliveryFree = response.data.cartAmountToTakeFreeDelivery
                    )
                )
            }
        } else {
            _totalCostUiState.update {
                it.copy(
                    isLoading = false,
                    isSucceedRemovePromo = false,
                    messageRemovePromo = response.message,
                    error = null,
                )
            }
        }
    }

    private fun onRemovePromoFailure(error: String) {
        _totalCostUiState.update {
            it.copy(
                isLoading = false,
                isSucceedApplyPromo = false,
                error = error,
                messageApplyPromo = null
            )
        }
    }

    fun navigateToOrder() {
        _totalCostUiState.update {
            it.copy(navigateToOrder = true, isSucceed = false, message = null)
        }
    }

    fun navigateToOrderDone() {
        _totalCostUiState.update {
            it.copy(navigateToOrder = false, isSucceed = false)
        }
    }

    fun navigateToMarket() {
        _totalCostUiState.update {
            it.copy(navigateToMarket = true)
        }
    }

    fun navigateToMarketDone() {
        _totalCostUiState.update {
            it.copy(navigateToMarket = false)
        }
    }

    fun resetMsg() {
        _totalCostUiState.update {
            it.copy(
                messageApplyPromo = null,
            )
        }
    }

    @AssistedFactory
    interface TotalCostAssistedFactory {
        fun create(addOrderUiState: AddOrderUiState, totalPoints: Double): TotalCostViewModel
    }

    companion object {
        fun createTotalCostFactory(
            assistedFactory: TotalCostAssistedFactory,
            addOrderUiState: AddOrderUiState,
            totalPoints: Double,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(addOrderUiState, totalPoints) as T
                }
            }
        }
    }
}