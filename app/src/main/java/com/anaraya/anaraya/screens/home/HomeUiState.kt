package com.anaraya.anaraya.screens.home

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.shared_data.Image
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import kotlinx.coroutines.flow.Flow


data class HomeUiState(
    val isLoading: Boolean = false,
    val navigateToSchedule: Boolean = false,
    val navigateToSearch: Boolean = false,
    val navigateToAllProduct: Boolean = false,
    val navigateToPointsProducts: Boolean = false,
    val visibilityTrending: Boolean = false,
    val visibilityPoints: Boolean = false,
    val isSucceedGetCartData: Boolean = false,
    val numOfBasket: Int = 0,
    val loyaltyPoints: Int = 0,
    val error: String? = null,
    val productsAdsUiState: List<ProductAdUiState> = emptyList(),
    val addCartUiState: String? = null,
    val addPointCartUiState: String? = null,
    val surveyImage: String? = null,
    val surveyId: Int = -1,
    val trendingText: String = "",
    val isSucceedAddCartUiState: Boolean = false,
    val isSucceedAddPointCartUiState: Boolean = false,
    val trendingProductUiState: Flow<PagingData<ProductUiState>>? = null,
    val pointsProductUiState: Flow<PagingData<ProductUiState>>? = null,
    val categoryUiState: Flow<PagingData<CategoryUiState>>? = null,
)

data class ProductAdUiState(
    override val image: String?, val id: Int,
    val adsLink: String? = null,
    val adsType: Int,
) : Image(image)


data class CategoryUiState(
    val id: Int,
    val image: String? = null,
    val name: String,
)