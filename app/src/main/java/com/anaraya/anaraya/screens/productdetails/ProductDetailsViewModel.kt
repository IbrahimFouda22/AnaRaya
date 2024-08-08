package com.anaraya.anaraya.screens.productdetails

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.category_product.toUiState
import com.anaraya.domain.entity.AddDeleteFav
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.ProductDetails
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageCartUseCase
import com.anaraya.domain.usecase.ManageFavUseCase
import com.anaraya.domain.usecase.ManageProductUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted val productId: Int,
    @Assisted val isPoints: Boolean,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val manageFavUseCase: ManageFavUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _product = MutableStateFlow(ProductDetailsUiState())
    val product = _product as StateFlow<ProductDetailsUiState>

    init {
        getProductById()
    }

    fun getAllDate() {
        getProductById()
    }

    private fun getProductById() {
        _product.update {
            ProductDetailsUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onSuccessGetProduct(manageProductUseCase.getProductById(productId))
            } catch (e: NoInternetException) {
                onFailureGetProduct(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onFailureGetProduct(e.message.toString())
            }
        }
    }

    private fun onSuccessGetProduct(product: ProductDetails) {
        _product.update {
            ProductDetailsUiState(
                isLoading = false,
                error = null,
                qtyInBasket = if (isPoints) product.data.qtyInLoyaltyBasket else product.data.qtyInBasket,
                availableQty = product.data.availableQty,
                favouriteStock = product.data.favouriteStock,
                price = if (isPoints) product.data.pointInRedeem else product.data.priceAfter,
                allPrice = if (isPoints) {
                    if (product.data.qtyInLoyaltyBasket == 0)
                        product.data.pointInRedeem
                    else
                        product.data.qtyInLoyaltyBasket * product.data.pointInRedeem
                } else {
                    if (product.data.qtyInBasket == 0)
                        product.data.priceAfter
                    else
                        product.data.qtyInBasket * product.data.priceAfter
                },
                visibilityRecommended = product.data.recommendedStocks.isNotEmpty(),
                recommendedList = product.data.recommendedStocks.map {
                    it.toUiState()
                },
                productDetailsUiState = ProductDetailsUiData(
                    name = product.data.description,
                    images = product.data.stockImages?.map {
                        ProductDetailsUiImages(image = it)
                    },
                    image = null,
                    price = if (isPoints) product.data.pointInRedeem else product.data.priceAfter,
                    beforeDiscount = product.data.priceBefore,
                    discount = product.data.discount,
                    availableQty = product.data.availableQty,
                    description = product.data.description,
                    specs1 = product.data.specs1,
                    specs2 = product.data.specs2,
                    qtyInBasket = if (isPoints) product.data.qtyInLoyaltyBasket else product.data.qtyInBasket,
                    inBasket = if (isPoints) product.data.isInLoyaltyBasket else product.data.inBasket,
                    notifyMe = product.data.notifyMe,
                    limitQtyForUserPerMonth = product.data.limitQtyForUserPerMonth,
                    qtyUsedFromLimit = product.data.qtyUsedFromLimit
                )
            )
        }
        buttonMinusState()
        buttonPlusState()
    }

    private fun onFailureGetProduct(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, productDetailsUiState = null
            )
        }
    }


    fun addProductToCart(productQty: Int, isUpdate: Boolean = true) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                btnMinusVisibility = false,
                btnPlusVisibility = false,
                messageAdd = null,
                messageDelete = null,
            )
        }
        viewModelScope.launch {
            try {
                onSuccessAddProductToCart(
                    if (isPoints)
                        manageCartUseCase.addPointCart(
                            productId, productQty
                        )
                    else
                        manageCartUseCase.addCart(
                            productId, productQty
                        ), isUpdate
                )
            } catch (e: NoInternetException) {
                onFailureAddProductToCart(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onFailureAddProductToCart(e.message.toString())
            }
        }
    }

    private fun onSuccessAddProductToCart(response: AddRemoveCart, isUpdate: Boolean = true) {
        if (!isUpdate) {
            _product.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    messageAdd = response.message,
                    isSucceedMessageAdd = response.isSucceed,
                    messageDelete = null,
                    productDetailsUiState = if (response.isSucceed) it.productDetailsUiState?.copy(
                        inBasket = true
                    ) else it.productDetailsUiState
                )
            }
        } else {
            _product.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    messageAdd = null,
                    messageDelete = null,
                )
            }
        }
        buttonMinusState()
        buttonPlusState()
        //getAllDate()
    }

    private fun onFailureAddProductToCart(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageAdd = null, isSucceedMessageAdd = false,
                messageDelete = null,
            )
        }
        getAllDate()
    }

    fun addProductRecommendedToCart(productId: Int, productQty: Int) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                messageAddRecommended = null,
            )
        }
        viewModelScope.launch {
            try {
                onAddProductRecommendedToCartSuccess(
                    manageCartUseCase.addCart(
                        productId, productQty
                    )
                )
            } catch (e: NoInternetException) {
                onAddProductRecommendedToCartFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddProductRecommendedToCartFailure(e.message.toString())
            }
        }
    }

    private fun onAddProductRecommendedToCartSuccess(response: AddRemoveCart) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                messageAddRecommended = response.message,
            )
        }
        getRecommended()
    }

    private fun onAddProductRecommendedToCartFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageAddRecommended = null
            )
        }
    }

    fun removeProductFromCart() {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                btnMinusVisibility = false,
                btnPlusVisibility = false,
                messageAdd = null,
                messageDelete = null,
            )
        }
        viewModelScope.launch {
            try {
                onSuccessRemoveProductFromCart(
                    if (isPoints)
                        manageCartUseCase.removeProductPointFromCart(
                            productId,
                        )
                    else
                        manageCartUseCase.removeProductFromCart(
                            productId,
                        )
                )
            } catch (e: NoInternetException) {
                onFailureRemoveProductFromCart(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onFailureRemoveProductFromCart(e.message.toString())
            }
        }
    }

    private fun onSuccessRemoveProductFromCart(response: AddRemoveCart) {
        _product.update {
            it.copy(
                isLoading = false, error = null, messageAdd = null,
                messageDelete = response.message,
            )
        }
        getAllDate()
    }

    private fun onFailureRemoveProductFromCart(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageDelete = null, messageAdd = null
            )
        }
        getAllDate()
    }

    fun plusQty() {
        if (_product.value.productDetailsUiState!!.limitQtyForUserPerMonth == 0) {
            if (_product.value.productDetailsUiState!!.inBasket) {
                if (_product.value.qtyInBasket < _product.value.availableQty) {
                    _product.update {
                        it.copy(
                            qtyInBasket = it.qtyInBasket + 1,
                            allPrice = (it.qtyInBasket + 1) * it.price,
                            btnPlusVisibility = true,
                            btnMinusVisibility = true
                        )

                    }
                    addProductToCart(_product.value.qtyInBasket)
                } else {
                    _product.update {
                        it.copy(btnPlusVisibility = false)
                    }
                }
            } else {
                if (_product.value.qtyInBasket < _product.value.availableQty) {
                    _product.update {
                        it.copy(
                            qtyInBasket = it.qtyInBasket + 1,
                            allPrice = (it.qtyInBasket + 1) * it.price,
                            btnPlusVisibility = true,
                            btnMinusVisibility = true
                        )
                    }
                    //addProductToCart(_product.value.qtyInBasket)
                    buttonPlusState()
                } else {
                    _product.update {
                        it.copy(btnPlusVisibility = false)
                    }
                }
            }
        } else {
            if (_product.value.productDetailsUiState!!.inBasket) {
                if (_product.value.qtyInBasket < _product.value.productDetailsUiState!!.limitQtyForUserPerMonth && _product.value.qtyInBasket < _product.value.availableQty) {
                    _product.update {
                        it.copy(
                            qtyInBasket = it.qtyInBasket + 1,
                            allPrice = (it.qtyInBasket + 1) * it.price,
                            btnPlusVisibility = true,
                            btnMinusVisibility = true
                        )

                    }
                    addProductToCart(_product.value.qtyInBasket)
                } else {
                    _product.update {
                        it.copy(btnPlusVisibility = false)
                    }
                }
            } else {
                if (_product.value.qtyInBasket < (_product.value.productDetailsUiState!!.limitQtyForUserPerMonth - _product.value.productDetailsUiState!!.qtyUsedFromLimit) && _product.value.qtyInBasket < _product.value.availableQty) {
                    _product.update {
                        it.copy(
                            qtyInBasket = it.qtyInBasket + 1,
                            allPrice = (it.qtyInBasket + 1) * it.price,
                            btnPlusVisibility = true,
                            btnMinusVisibility = true
                        )
                    }
                    //addProductToCart(_product.value.qtyInBasket)
                    buttonPlusState()
                } else {
                    _product.update {
                        it.copy(btnPlusVisibility = false)
                    }
                }
            }
        }
    }

    fun minusQty() {
        if (_product.value.productDetailsUiState!!.inBasket && _product.value.qtyInBasket > 1) {
            _product.update {
                it.copy(
                    qtyInBasket = it.qtyInBasket - 1,
                    allPrice = (it.qtyInBasket - 1) * it.price,
                    btnMinusVisibility = true,
                    btnPlusVisibility = true,
                )
            }
            addProductToCart(_product.value.qtyInBasket)
        } else {
            if (_product.value.qtyInBasket > 1) {
                _product.update {
                    it.copy(
                        qtyInBasket = it.qtyInBasket - 1,
                        allPrice = (it.qtyInBasket - 1) * it.price,
                        btnMinusVisibility = true,
                        btnPlusVisibility = true,
                    )
                }
                //addProductToCart(_product.value.qtyInBasket)
                buttonMinusState()
            } else {
                _product.update {
                    it.copy(btnMinusVisibility = false)
                }
            }
        }
    }

    private fun buttonMinusState() {
        if (_product.value.qtyInBasket > 1) _product.update {
            it.copy(btnMinusVisibility = true, messageAdd = null)
        }
        else _product.update {
            it.copy(btnMinusVisibility = false, messageAdd = null)
        }
    }

    private fun buttonPlusState() {
//        if (_product.value.productDetailsUiState!!.limitQtyForUserPerMonth == 0) {
        if (_product.value.qtyInBasket < _product.value.availableQty) {
            _product.update {
                it.copy(btnPlusVisibility = true, messageAdd = null)
            }
        } else _product.update {
            it.copy(btnPlusVisibility = false, messageAdd = null)
        }
//        } else {
//            if (_product.value.qtyInBasket < _product.value.availableQty && _product.value.qtyInBasket < (_product.value.productDetailsUiState!!.limitQtyForUserPerMonth - _product.value.productDetailsUiState!!.qtyUsedFromLimit)) {
//                _product.update {
//                    it.copy(btnPlusVisibility = true, messageAdd = null)
//                }
//            } else _product.update {
//                it.copy(btnPlusVisibility = false, messageAdd = null)
//            }
//        }
    }

    fun favButton() {
        if (product.value.productDetailsUiState != null) {
            if (product.value.favouriteStock) deleteFromFav()
            else addToFav()
        }
    }

    private fun addToFav() {
        _product.update {
            it.copy(isLoading = true, error = null, messageAddDeleteFav = null)
        }
        viewModelScope.launch {
            try {
                onSuccessAddToFav(
                    manageFavUseCase.addToFav(
                        productId,
                    )
                )
            } catch (e: NoInternetException) {
                onFailureAddToFav(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onFailureAddToFav(e.message.toString())
            }
        }
    }

    private fun onSuccessAddToFav(response: AddDeleteFav) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                messageAddDeleteFav = response.message,
                favouriteStock = !it.favouriteStock
            )
        }
        //getAllDate()
    }

    private fun onFailureAddToFav(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageAddDeleteFav = null
            )
        }
        //getAllDate()
    }

    private fun deleteFromFav() {
        _product.update {
            it.copy(isLoading = true, error = null, messageAddDeleteFav = null)
        }
        viewModelScope.launch {
            try {
                onSuccessDeleteFromFav(
                    manageFavUseCase.deleteFromFav(
                        productId,
                    )
                )
            } catch (e: NoInternetException) {
                onFailureDeleteFromFav(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onFailureDeleteFromFav(e.message.toString())
            }
        }
    }

    private fun onSuccessDeleteFromFav(response: AddDeleteFav) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                messageAddDeleteFav = response.message,
                favouriteStock = !it.favouriteStock
            )
        }
        //getAllDate()
    }

    private fun onFailureDeleteFromFav(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageAddDeleteFav = null
            )
        }
        // getAllDate()
    }

    fun changeNotify() {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                messageNotify = null,
            )
        }
        if (_product.value.productDetailsUiState?.notifyMe == true) {
            removeProductToNotifyMe(productId)
        } else
            addProductToNotifyMe(productId)
    }

    private fun addProductToNotifyMe(productId: Int) {
        viewModelScope.launch {
            try {
                onAddProductToNotifyMeSuccess(
                    manageProductUseCase.addToNotifyMe(
                        productId
                    )
                )
            } catch (e: NoInternetException) {
                onAddProductToNotifyMeFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddProductToNotifyMeFailure(e.message.toString())
            }
        }
    }

    private fun onAddProductToNotifyMeSuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                messageNotify = response.message,
                isSucceedNotify = response.isSucceed,
                productDetailsUiState = it.productDetailsUiState?.copy(
                    notifyMe = if (response.isSucceed) !it.productDetailsUiState.notifyMe else it.productDetailsUiState.notifyMe
                )
            )
        }
    }

    private fun onAddProductToNotifyMeFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageNotify = null, isSucceedNotify = false
            )
        }
    }

    private fun removeProductToNotifyMe(productId: Int) {
        viewModelScope.launch {
            try {
                onRemoveProductToNotifyMeSuccess(
                    manageProductUseCase.removeFromNotifyMe(
                        productId
                    )
                )
            } catch (e: NoInternetException) {
                onRemoveProductToNotifyMeFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onRemoveProductToNotifyMeFailure(e.message.toString())
            }
        }
    }

    private fun onRemoveProductToNotifyMeSuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                messageNotify = response.message,
                isSucceedNotify = response.isSucceed,
                productDetailsUiState = it.productDetailsUiState?.copy(
                    notifyMe = if (response.isSucceed) !it.productDetailsUiState.notifyMe else it.productDetailsUiState.notifyMe
                )
            )
        }
    }

    private fun onRemoveProductToNotifyMeFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, messageNotify = null, isSucceedNotify = false
            )
        }
    }

    private fun getRecommended() {
        _product.update {
            ProductDetailsUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onSuccessGetRecommendedProduct(manageProductUseCase.getProductById(productId))
            } catch (e: NoInternetException) {
                onFailureGetRecommendedProduct(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onFailureGetRecommendedProduct(e.message.toString())
            }
        }
    }

    private fun onSuccessGetRecommendedProduct(product: ProductDetails) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                visibilityRecommended = product.data.recommendedStocks.isNotEmpty(),
                recommendedList = product.data.recommendedStocks.map { product ->
                    product.toUiState()
                },
            )
        }
    }

    private fun onFailureGetRecommendedProduct(error: String) {
        _product.update {
            it.copy(
                isLoading = false, error = error, productDetailsUiState = null
            )
        }
    }

    fun navigateToCart() {
        _product.update {
            it.copy(navigateToCart = true)
        }
    }

    fun navigateToCartDone() {
        _product.update {
            it.copy(navigateToCart = false)
        }
    }

    fun navigateToProductDetails() {
        _product.update {
            it.copy(navigateToProductDetails = true)
        }
    }

    fun navigateToProductDetailsDone() {
        _product.update {
            it.copy(navigateToProductDetails = false)
        }
    }

    fun resetAddRecommendedCartMessage() {
        _product.update {
            it.copy(
                messageAddRecommended = null
            )
        }
    }

    fun resetAddFavMessage() {
        _product.update {
            it.copy(
                messageAddDeleteFav = null
            )
        }
    }

    fun resetAddCartMessage() {
        _product.update {
            it.copy(
                messageAdd = null
            )
        }
    }

    fun resetRemoveCartMessage() {
        _product.update {
            it.copy(
                messageDelete = null
            )
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(productId: Int, isPoints: Boolean): ProductDetailsViewModel
    }

    companion object {
        fun createFactory(
            productId: Int, isPoints: Boolean, assistedFactory: AssistedFactory,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(productId, isPoints) as T
                }
            }
        }
    }
}