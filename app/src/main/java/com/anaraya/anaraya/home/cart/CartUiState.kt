package com.anaraya.anaraya.home.cart

import com.anaraya.anaraya.home.address.AddressUiStateData
import com.anaraya.anaraya.home.address.ChangeAddressUiState
import com.anaraya.anaraya.home.shared_data.AddOrderUiState
import com.anaraya.domain.entity.Cart
import java.io.Serializable


data class CartUiState(
    val isLoading: Boolean = false,
    val isSucceedGetCartData: Boolean = false,
    val error: String? = null,
    val showEmptyCart: Boolean = false,
    val navigateToSchedule: Boolean = false,
    val navigateToAddress: Boolean = false,
    val navigateToMarket: Boolean = false,
    val navigateToTotalCost: Boolean = false,
    val deleteMsg: String? = null,
    val updateMsg: String? = null,
    val cartUiState: CartUiData? = null,
    val checkOutUiState: CheckOutUiStateData? = null,
    val addOrderUiState: AddOrderUiState? = null,
    val addressUiState: List<AddressUiStateData> = emptyList(),
    val companyAddressUiState: List<AddressUiStateData> = emptyList(),
    val allAddressesUiState: List<AddressUiStateData> = emptyList(),
    val errorChangeDefaultAddress: String? = null,
    val changeAddressUiState: ChangeAddressUiState? = null,
    val isSucceedChangeAddress: Boolean = false,
    val selectedMethod: String? = null
) : Serializable

data class CartUiData(
    val cartUiListState: List<CartUiList> = emptyList(),
    val cartTotal: Double = 0.0,
    val cartDeliveryFee: Double = 0.0,
    val cartTotalAmount: Double = 0.0,
    val cartPromoCodeDiscount: Double = 0.0,
    val cartAmountToTakeFreeDelivery: Double = 0.0,
    val hasAddress: Boolean,
)

data class CartUiList(
    val id: Int,
    val price: Double,
    val qty: Int,
    val name: String,
    val desc: String,
    val availableQty: Int,
    val stockImg: String? = null,
) {
    var plus = qty < availableQty
    var minus = qty > 1
}

data class CheckOutUiStateData(
    val isSucceedGetCheckOut: Boolean = false,
    val deliveryAddressLabel: String?,
    val totalCost: Double,
    val paymentMethods: List<String> = emptyList(),
)

fun Cart.toUiState(): CartUiData {
    return CartUiData(
        cartTotal = data.cartTotal,
        cartPromoCodeDiscount = data.cartPromoCodeDiscount,
        cartDeliveryFee = data.cartDeliveryFee,
        cartTotalAmount = data.cartTotalAmount,
        cartAmountToTakeFreeDelivery = data.cartAmountToTakeFreeDelivery,
        hasAddress = data.hasAddress,
        cartUiListState = data.data.map { data ->
            CartUiList(
                id = data.id,
                price = data.totalProductPrice,
                qty = data.qty,
                name = data.specs1,
                desc = data.specs2,
                stockImg = data.stockImg,
                availableQty = data.productAvailableQty
            )
        }
    )
}