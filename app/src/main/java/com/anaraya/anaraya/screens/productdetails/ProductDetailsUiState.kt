package com.anaraya.anaraya.screens.productdetails

import com.anaraya.anaraya.screens.shared_data.Image
import com.anaraya.anaraya.screens.shared_data.ProductUiState

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val navigateToProductDetails: Boolean = false,
    val error: String? = null,
    val visibilityRecommended: Boolean = false,
    val isSucceedMessageAdd: Boolean = false,
    val recommendedList: List<ProductUiState> = emptyList(),
    val messageAdd: String? = null,
    val messageDelete: String? = null,
    val messageAddRecommended: String? = null,
    val messageNotify: String? = null,
    val isSucceedNotify: Boolean = false,
    val messageAddDeleteFav: String? = null,
    val productDetailsUiState: ProductDetailsUiData? = null,
    val qtyInBasket: Int = 0,
    val allPrice: Double = 1.0,
    val price: Double = 1.0,
    val availableQty: Int = 1,
    val btnPlusVisibility: Boolean = true,
    val btnMinusVisibility: Boolean = true,
    val navigateToCart: Boolean = false,
    val favouriteStock: Boolean = false,
)

data class ProductDetailsUiData(
    val image: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val discount: Double,
    val availableQty: Int,
    val beforeDiscount: Double,
    val specs1: String,
    val specs2: String,
    val images: List<ProductDetailsUiImages>? = null,
    val inBasket: Boolean,
    val qtyInBasket: Int,
    val limitQtyForUserPerMonth: Int,
    val qtyUsedFromLimit: Int,
    val notifyMe: Boolean = false,
)

data class ProductDetailsUiImages(
    override val image: String?,
) : Image(image)
