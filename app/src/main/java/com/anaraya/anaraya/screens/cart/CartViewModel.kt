package com.anaraya.anaraya.screens.cart

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.screens.address.AddressUiStateData
import com.anaraya.anaraya.screens.address.ChangeAddressUiState
import com.anaraya.anaraya.screens.address.toAddressUiState
import com.anaraya.anaraya.screens.shared_data.AddOrderUiState
import com.anaraya.anaraya.screens.shared_data.toUiState
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.Addresses
import com.anaraya.domain.entity.Cart
import com.anaraya.domain.entity.ChangeDefaultAddress
import com.anaraya.domain.entity.CheckOut
import com.anaraya.domain.exception.IllegalStateException
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
    private val manageAddressesUseCase: ManageAddressesUseCase,
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
        if (cart.data.data.isEmpty() && cart.data.loyaltyData.isEmpty()) {
            _cartUiState.update {
                it.copy(
                    isLoading = false,
                    showEmptyCart = true,
                    error = null,
                    isSucceedGetCartData = true,
                    cartUiState = null,
                    visibilityPoints = false,
                    totalPoints = 0.0,
                )
            }
        } else {
            _cartUiState.update {
                it.copy(
                    isLoading = false,
                    showEmptyCart = false,
                    error = null,
                    isSucceedGetCartData = true,
                    cartUiState = cart.toUiState(),
                    visibilityPoints = cart.data.loyaltyData.isNotEmpty(),
                    totalPoints = cart.data.loyaltyData.sumOf { item -> item.pointInRedeem * item.qty },
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
                showEmptyCart = false,
                totalPoints = 0.0,
                visibilityPoints = false
            )
        }
    }

    fun getAddresses() {
        _cartUiState.update {
            it.copy(
                isLoading = true,
                isSucceedGetCartData = false,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null
            )
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

        if (_cartUiState.value.allAddressesUiState.isEmpty()) {
            _cartUiState.update {
                it.copy(
                    showEmptyAddress = true,
                    defaultAddressTitle = ""
                )
            }
        } else {
            for (item in _cartUiState.value.allAddressesUiState) {
                if (item.defaultAddress) {
                    _cartUiState.update {
                        it.copy(
                            showEmptyAddress = false,
                            defaultAddressTitle = item.addressUiState.addressLabel,
                        )
                    }
                    break
                }
            }
        }
    }

    private fun onGetAddressesFailure(error: String) {
        _cartUiState.update {
            it.copy(
                error = error,
            )
        }
    }

    fun removeProduct(productId: Int, isLoyalty: Boolean) {
        _cartUiState.update {
            it.copy(
                isLoading = true, error = null,
                deleteMsg = null,
                isSucceedDeleteProduct = false,
                isPoints = isLoyalty
            )
        }
        viewModelScope.launch {
            try {
                if (isLoyalty)
                    onRemoveProductSuccess(manageCartUseCase.removeProductPointFromCart(productId))
                else
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
                deleteMsg = response.message,
                isSucceedDeleteProduct = response.isSucceed
            )
        }
        getCartData()
    }

    private fun onRemoveProductFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false, error = error,
                deleteMsg = null,
                isSucceedDeleteProduct = false
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
            } catch (_: IllegalStateException) {

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
                    paymentMethods = checkOut.data.paymentMethods,
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

    fun addProductToCart(productId: Int, productQty: Int, isLoyalty: Boolean) {
        _cartUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                updateMsg = null,
            )
        }
        viewModelScope.launch {
            try {
                if (isLoyalty)
                    onSuccessAddProductToCart(
                        manageCartUseCase.addPointCart(
                            productId,
                            productQty
                        )
                    )
                else
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
                        amountToTakeDeliveryFree = it.cartUiState.cartAmountToTakeFreeDelivery,
                        promoCode = it.cartUiState.cartPromoCode
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
                isLoading = true, error = null,
                errorChangeDefaultAddress = null,
                isSucceedChangeAddress = response.isSucceed,
                changeAddressUiState = ChangeAddressUiState(
                    message = response.message,
                    statusCode = response.statusCode
                )
            )
        }
        if (response.isSucceed)
            getCartDataToUpdateTotals()
    }


    private fun onChangeDefaultAddressFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false, error = null, changeAddressUiState = null,
                errorChangeDefaultAddress = error, isSucceedChangeAddress = false
            )
        }
    }

    private fun getCartDataToUpdateTotals() {
        viewModelScope.launch {
            try {
                onGetProductsToUpdateTotalsSuccess(manageCartUseCase.getCart())
            } catch (e: NoInternetException) {
                onGetProductsToUpdateTotalsFailure("No Internet")
            } catch (e: Exception) {
                onGetProductsToUpdateTotalsFailure(e.message.toString())
            }
        }
    }

    private fun onGetProductsToUpdateTotalsSuccess(cart: Cart) {
        _cartUiState.update {
            it.copy(
                error = null,
                isSucceedGetCartData = true,
                cartUiState = it.cartUiState?.copy(
                    cartTotal = cart.data.cartTotal,
                    cartPromoCodeDiscount = cart.data.cartPromoCodeDiscount,
                    cartDeliveryFee = cart.data.cartDeliveryFee,
                    cartTotalAmount = cart.data.cartTotalAmount,
                    cartAmountToTakeFreeDelivery = cart.data.cartAmountToTakeFreeDelivery,
                    hasAddress = cart.data.hasAddress,
                ),
            )
        }
        getCheckOutAndUpdateTotals()
    }

    private fun onGetProductsToUpdateTotalsFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                cartUiState = null,
                isSucceedGetCartData = false,
                showEmptyCart = false,
                totalPoints = 0.0,
                visibilityPoints = false
            )
        }
    }

    private fun getCheckOutAndUpdateTotals() {
        viewModelScope.launch {
            try {
                onGetCheckOutAndUpdateTotalsSuccess(manageCartUseCase.getCheckOut())
            } catch (_: IllegalStateException) {

            } catch (e: NoInternetException) {
                onGetCheckOutAndUpdateTotalsFailure("No Internet")
            } catch (e: Exception) {
                onGetCheckOutAndUpdateTotalsFailure(e.message.toString())
            }
        }
    }

    private fun onGetCheckOutAndUpdateTotalsSuccess(checkOut: CheckOut) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                checkOutUiState = CheckOutUiStateData(
                    isSucceedGetCheckOut = checkOut.isSucceed,
                    deliveryAddressLabel = checkOut.data.deliveryAddressLabel,
                    totalCost = checkOut.data.totalCost,
                )
            )
        }
        fillAddOrder()
    }

    private fun onGetCheckOutAndUpdateTotalsFailure(error: String) {
        _cartUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                checkOutUiState = null
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

    fun resetMsg() {
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