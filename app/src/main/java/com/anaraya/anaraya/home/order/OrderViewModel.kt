package com.anaraya.anaraya.home.order

import androidx.lifecycle.ViewModel
import com.anaraya.domain.usecase.ManageCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val manageCartUseCase: ManageCartUseCase
) : ViewModel() {

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val orderUiState = _orderUiState as StateFlow<OrderUiState>


    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome = _navigateToHome as StateFlow<Boolean>

//    private val _navigateToMarket = MutableStateFlow(false)
//    val navigateToMarket = _navigateToMarket as StateFlow<Boolean>

   /* init {
        getCartData()
    }

    fun getAllData() {
        getCartData()

    }

    private fun getCartData() {
        _totalCostUiState.update {
            TotalCostUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetProductsSuccess(manageCartUseCase.getCart().cachedIn(viewModelScope))
            } catch (e: NoInternetException) {
                onGetProductsFailure("No Internet")
            } catch (e: Exception) {
                onGetProductsFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductsSuccess(list: Flow<PagingData<Cart>>) {
        _totalCostUiState.update {
            TotalCostUiState(
                isLoading = false,
                error = null,
            )
        }
    }


    private fun onGetProductsFailure(error: String) {
        _totalCostUiState.update {
            TotalCostUiState(
                isLoading = false,
                error = error,
            )
        }
    }
*/

    fun navigateToHome() {
        _navigateToHome.update {
            true
        }
    }

    fun navigateToHomerDone() {
        _navigateToHome.update {
            false
        }
    }

//    fun navigateToMarket() {
//        _navigateToMarket.update {
//            true
//        }
//    }
//
//    fun navigateToMarketDone() {
//        _navigateToOrder.update {
//            false
//        }
//    }

}