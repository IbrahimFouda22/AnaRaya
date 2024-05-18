package com.anaraya.anaraya.home.category_product

import androidx.paging.PagingData
import com.anaraya.anaraya.home.shared_data.ProductUiState
import com.anaraya.domain.entity.Product
import kotlinx.coroutines.flow.Flow

data class CategoryProductUiState(
    val isLoading: Boolean = false,
    val navigateToCart: Boolean = false,
    val error: String? = null,
    //val products: List<ProductUiState> = arrayListOf(),
    val products: Flow<PagingData<ProductUiState>>?=null,
    val addCartUiState: String? = null,
    //val updateProducts: Flow<PagingData<ProductUiState>>?=null,
    val categories: List<CategoryUiState> = arrayListOf()
)

data class CategoryUiState(
    val id:Int,
    val name: String
)

fun Product.toUiState():ProductUiState{
    return ProductUiState(
        image = image,
        price = priceAfter,
        priceBeforeDiscount = priceBefore,
        name = name,
        qty = availableQty,
        discount = discount,
        catName = category,
        id = id,
        specs1 = specs1,
        specs2 = specs2,
        availableQty = availableQty,
        inBasket = inBasket,
        limitQtyForUserPerMonth = limitQtyForUserPerMonth,
        qtyUsedFromLimit = limitQtyForUserPerMonth
    )
}

//fun ProductUiState.toProduct():Product{
//    return Product(
//
//        image = image,
//        priceAfter = price,
//        priceBefore = priceBeforeDiscount,
//        name = name,
//        qty = qty,
//        discount = discount,
//        itemCode = productCode,
//        category = catName,
//        brand = "",
//        availableQty = 0.0,
//
//    )
//}
