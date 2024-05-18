package com.anaraya.anaraya.home.cart

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.home.address.AddressUiStateData
import com.anaraya.anaraya.home.address.ChangeAddressUiState
import com.anaraya.anaraya.home.address.toAddressUiState
import com.anaraya.anaraya.home.shared_data.AddOrderUiState
import com.anaraya.anaraya.home.shared_data.toUiState
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.Addresses
import com.anaraya.domain.entity.Cart
import com.anaraya.domain.entity.ChangeDefaultAddress
import com.anaraya.domain.entity.CheckOut
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import com.anaraya.domain.usecase.ManageCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val manageCartUseCase: ManageCartUseCase,
    private val manageAddressesUseCase: ManageAddressesUseCase
) : ViewModel() {
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState
    private val _navigateToAddAddress = MutableStateFlow(false)
    val navigateToAddAddress: StateFlow<Boolean> = _navigateToAddAddress
    init {
        getCartData()
        getAddresses()
    }
    fun getAllData() {
        getCartData()
        getAddresses()
    }
    private fun getCartData() {
        _cartUiState.update {
            CartUiState(
                isLoading = true,
                error = null,
                showEmptyCart = false,
                cartUiState = null
            )
        }
        viewModelScope.launch {
            try {
                onGetProductsSuccess(manageCartUseCase.getCart())
            } catch (e: NoInternetException) {
                onGetProductsFailure("No Internet")
            } catch (e: Exception) {
                onGetProductsFailure(e.message.toString())
            }
        }
    }
    private fun onGetProductsSuccess(cart: Cart) {
        if (cart.data.data.isEmpty()) {
            _cartUiState.update {
                it.copy(
                    isLoading = false,
                    showEmptyCart = true,
                    error = null,
                    isSucceedGetCartData = true,
                    cartUiState = null
                )
            }
        } else {
            _cartUiState.update {
                it.copy(
                    isLoading = false,
                    showEmptyCart = false,
                    error = null,
                    isSucceedGetCartData = true,
                    cartUiState = cart.toUiState()
                )
            }
            getCheckOut()
        }
    }
    private fun onGetProductsFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                cartUiState = null,
                isSucceedGetCartData = false,
                showEmptyCart = false
            )
        }
    }
    fun getAddresses() {
        _cartUiState.update {
            it.copy(isLoading = true,isSucceedGetCartData = false, changeAddressUiState = null, errorChangeDefaultAddress = null)
        }
        resetMsg()
        viewModelScope.launch {
            try {
                onGetAddressesSuccess(manageAddressesUseCase.getAddresses())
            } catch (e: NoInternetException) {
                onGetAddressesFailure("No Internet")
            } catch (e: Resources.NotFoundException) {
                onGetAddressesFailure("No Titles")
            } catch (e: Exception) {
                onGetAddressesFailure(e.message.toString())
            }
        }
    }
    private fun onGetAddressesSuccess(addresses: Addresses) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                addressUiState = addresses.data.map { data ->
                    AddressUiStateData(
                        defaultAddress = data.defaultAddress,
                        isUserAddress = true,
                        addressUiState = data.toUiState(),
                    )
                },
                companyAddressUiState = addresses.companyAddresses.map { companyAddress ->
                    companyAddress.toAddressUiState()
                },
            )
        }
        _cartUiState.update {
            it.copy(
                allAddressesUiState = it.addressUiState + it.companyAddressUiState
            )
        }
    }
    private fun onGetAddressesFailure(error: String) {
        _cartUiState.update {
            it.copy(
                error = error,
            )
        }
    }
    fun removeProduct(productId: Int) {
        _cartUiState.update {
            it.copy(
                isLoading = true, error = null,
                deleteMsg = null
            )
        }
        viewModelScope.launch {
            try {
                onRemoveProductSuccess(manageCartUseCase.removeProductFromCart(productId))
            } catch (e: NoInternetException) {
                onRemoveProductFailure("No Internet")
            } catch (e: Exception) {
                onRemoveProductFailure(e.message.toString())
            }
        }
    }
    private fun onRemoveProductSuccess(response: AddRemoveCart) {
        _cartUiState.update {
            it.copy(
                deleteMsg = response.message
            )
        }
        getCartData()
    }
    private fun onRemoveProductFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false, error = error,
                deleteMsg = null
            )
        }
    }
    private fun getCheckOut() {
        _cartUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                checkOutUiState = null
            )
        }
        viewModelScope.launch {
            try {
                onGetCheckOutSuccess(manageCartUseCase.getCheckOut())
            } catch (e: NoInternetException) {
                onGetCheckOutFailure("No Internet")
            } catch (e: Exception) {
                onGetCheckOutFailure(e.message.toString())
            }
        }
    }
    private fun onGetCheckOutSuccess(checkOut: CheckOut) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                checkOutUiState = CheckOutUiStateData(
                    isSucceedGetCheckOut = checkOut.isSucceed,
                    deliveryAddressLabel = checkOut.data.deliveryAddressLabel,
                    totalCost = checkOut.data.totalCost,
                    paymentMethods = checkOut.data.paymentMethods
                )
            )
        }
        fillAddOrder()
    }
    private fun onGetCheckOutFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                checkOutUiState = null
            )
        }
    }
    fun addProductToCart(productId: Int, productQty: Int) {
        _cartUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                updateMsg = null,
            )
        }
        viewModelScope.launch {
            try {
                onSuccessAddProductToCart(
                    manageCartUseCase.addCart(
                        productId,
                        productQty
                    )
                )
            } catch (e: NoInternetException) {
                onFailureAddProductToCart("No Internet")
            } catch (e: Exception) {
                onFailureAddProductToCart(e.message.toString())
            }
        }
    }
    private fun onSuccessAddProductToCart(response: AddRemoveCart) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                updateMsg = response.message,
            )
        }
        getCartData()
    }
    private fun onFailureAddProductToCart(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                updateMsg = null
            )
        }
        getCartData()
    }
    private fun fillAddOrder() {
        try {
            _cartUiState.update {
                it.copy(
                    addOrderUiState = AddOrderUiState(
                        subTotal = it.cartUiState!!.cartTotal + it.cartUiState.cartPromoCodeDiscount,
                        shipping = it.cartUiState.cartDeliveryFee,
                        total = it.cartUiState.cartTotalAmount,
                        discount = it.cartUiState.cartPromoCodeDiscount,
                        numOfItems = it.cartUiState.cartUiListState.size,
                        fee = it.cartUiState.cartDeliveryFee,
                        amountToTakeDeliveryFree = it.cartUiState.cartAmountToTakeFreeDelivery
                    )
                )
            }
        } catch (e: Exception) {
            _cartUiState.update {
                it.copy(error = "Server Error")
            }
        }
    }

    fun changeDefaultAddress(addressId: String, isUserAddress: Boolean) {
        _cartUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null,
            )
        }
        viewModelScope.launch {
            try {
                onChangeDefaultAddressSuccess(
                    manageAddressesUseCase.changeAddressDefault(
                        addressId,
                        isUserAddress
                    )
                )
            } catch (e: NoInternetException) {
                onChangeDefaultAddressFailure("No Internet")
            } catch (e: Exception) {
                onChangeDefaultAddressFailure(e.message.toString())
            }
        }
    }


    private fun onChangeDefaultAddressSuccess(response: ChangeDefaultAddress) {
        _cartUiState.update {
            it.copy(
                isLoading = false, error = null,
                errorChangeDefaultAddress = null,
                isSucceedChangeAddress = response.isSucceed,
                changeAddressUiState = ChangeAddressUiState(
                    message = response.message,
                    statusCode = response.statusCode
                )
            )
        }
    }


    private fun onChangeDefaultAddressFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false, error = null, changeAddressUiState = null,
                errorChangeDefaultAddress = error, isSucceedChangeAddress = false
            )
        }
    }
    fun navigateToSchedule() {
        _cartUiState.update {
            it.copy(navigateToSchedule = true)
        }
    }
    fun navigateToScheduleDone() {
        _cartUiState.update {
            it.copy(navigateToSchedule = false)
        }
    }
    fun navigateToAddAddress() {
        _navigateToAddAddress.update {
            true
        }
    }

    fun navigateToAddAddressDone() {
        _navigateToAddAddress.update {
            false
        }
    }
    fun navigateToMarket() {
        _cartUiState.update {
            it.copy(navigateToMarket = true)
        }
    }
    fun navigateToMarketDone() {
        _cartUiState.update {
            it.copy(navigateToMarket = false)
        }
    }
    fun navigateToTotalCost() {
        _cartUiState.update {
            it.copy(navigateToTotalCost = true)
        }
    }
    fun navigateToTotalCostDone() {
        _cartUiState.update {
            it.copy(navigateToTotalCost = false)
        }
    }
    fun resetMsg(){
        _cartUiState.update {
            it.copy(errorChangeDefaultAddress = null, changeAddressUiState = null)
        }
    }
    fun setSelectedMethod(method: String) {
        _cartUiState.update {
            it.copy(
                selectedMethod = method,
            )
        }
    }
}