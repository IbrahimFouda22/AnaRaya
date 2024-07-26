package com.anaraya.anaraya.screens.services.store.product.explore.explore_products

import androidx.paging.PagingData
import com.anaraya.domain.entity.ExploreProduct
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

data class ExploreProductUiState(
    val isLoading: Boolean = false,
    val navigateToProductDetails: Boolean = false,
    val error: String? = null,
    val products: Flow<PagingData<ExploreProductUiDetails>>? = null,
    val subCategories: List<CategoryUiState> = arrayListOf(),
)

data class CategoryUiState(
    val id: Int,
    val name: String,
)

fun ExploreProduct.toUiState(): ExploreProductUiDetails {
    return ExploreProductUiDetails(
        category = category,
        id = id,
        subCategory = subCategory,
        itemDescription = itemDescription,
        isUserProduct = isUserProduct,
        title = title,
        productImageUrl = productImageUrl,
        condition = condition,
        sellingStatus = sellingStatus,
        price = price,
        location = location,
        sellerName = sellerName,
        sellerPhoneNumber = sellerPhoneNumber
    )
}

data class ExploreProductUiDetails(
    val id:Int,
    val category:String,
    val subCategory:String,
    val condition:Int,
    val title:String,
    val itemDescription:String,
    val price:Double,
    val location:String,
    val productImageUrl:String?,
    val sellerName:String,
    val sellerPhoneNumber:String,
    val sellingStatus:Int,
    val isUserProduct:Boolean,
):Serializable

