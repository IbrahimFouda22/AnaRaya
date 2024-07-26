package com.anaraya.anaraya.screens.services.store.product.my_items.edit_product

import com.anaraya.anaraya.screens.home.CategoryUiState
import com.anaraya.anaraya.screens.services.store.product.my_items.ProductStoreItemState

data class EditItemServiceUiState(
    val isLoading:Boolean = false,
    val isSucceed: Boolean = false,
    val isSucceedEditProduct: Boolean = false,
    val addProductMsg:String? = null,
    val error:String?=null,
    val sellCategoriesList: List<CategoryUiState> = emptyList(),
    val sellSubCategoriesList: List<CategoryUiState> = emptyList(),
    val titleError: Boolean = false,
    val categoryError: Boolean = false,
    val subCategoryError: Boolean = false,
    val conditionError: Boolean = false,
    val descriptionError: Boolean = false,
    val priceError: Boolean = false,
    val locationError: Boolean = false,
    val anonymousError: Boolean = false,
    val deliveryError: Boolean = false,
    val pictureError: Boolean = false,
    val conditionAndTermsError: Boolean = false,
    val focusError: Boolean = false,
    val product: ProductStoreItemState? = null
)
