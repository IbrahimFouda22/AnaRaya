package com.anaraya.anaraya.screens.services.store.product.explore.product_details

import com.anaraya.anaraya.screens.services.store.product.explore.explore_products.ExploreProductUiDetails
import com.anaraya.domain.entity.ProductStoreForCustomer


data class ExploreProductDetailsUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val visibilitySellerInfo: Boolean = false,
    val visibilityButton: Boolean = false,
    val isSucceedRequestToBuy: Boolean = false,
    val requestToBuyMessage: String? = null,
    val error: String? = null,
    val product: ExploreProductUiDetails? = null,
)

fun ProductStoreForCustomer.toUiState(): ExploreProductUiDetails {
    return ExploreProductUiDetails(
        id = id,
        isUserProduct = isUserProduct,
        sellingStatus = sellingStatus,
        sellerName = sellerName,
        sellerPhoneNumber = sellerPhoneNumber,
        category = category,
        subCategory = category,
        location = category,
        title = title,
        condition = condition,
        itemDescription = itemDescription,
        price = price,
        productImageUrl = productImageUrl
    )
}