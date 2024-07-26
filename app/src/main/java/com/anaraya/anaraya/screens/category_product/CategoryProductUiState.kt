package com.anaraya.anaraya.screens.category_product

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import com.anaraya.domain.entity.Product
import kotlinx.coroutines.flow.Flow

data class CategoryProductUiState(
    val isLoading: Boolean = false,
    val navigateToCart: Boolean = false,
    val addCartSucceed: Boolean = false,
    val error: String? = null,
    val numOfBasket: Int = 0,
    val products: Flow<PagingData<ProductUiState>>? = null,
    val addCartUiState: String? = null,
    val categories: List<CategoryUiState> = arrayListOf(),
)

data class CategoryUiState(
    val id: Int,
    val name: String,
)

fun Product.toUiState(isPoints: Boolean = false): ProductUiState {
    return ProductUiState(
        image = image,
        price = if(isPoints) pointInRedeem else priceAfter,
        priceBeforeDiscount = priceBefore,
        name = name,
        qty = availableQty,
        discount = discount,
        catName = category,
        id = id,
        specs1 = specs1,
        specs2 = specs2,
        availableQty = availableQty,
        inBasket = if(isPoints) inLoyaltyBasket else inBasket,
        limitQtyForUserPerMonth = limitQtyForUserPerMonth,
        qtyUsedFromLimit = limitQtyForUserPerMonth,
        isPoints = isPoints,
        textTrending = textTrending
    )
}

