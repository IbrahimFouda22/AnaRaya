package com.anaraya.anaraya.screens.services.store.service.sell

import com.anaraya.anaraya.screens.home.CategoryUiState

data class SellServicesUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val isServiceRental: Boolean = false,
    val addProductMsg: String? = null,
    val error: String? = null,
    val sellCategoriesList: List<CategoryUiState> = emptyList(),
    val sellSubCategoriesList: List<CategoryUiState> = emptyList(),
    val titleError: Boolean = false,
    val categoryError: Boolean = false,
    val subCategoryError: Boolean = false,
    val descriptionError: Boolean = false,
    val priceError: Boolean = false,
    val locationError: Boolean = false,
    val pictureError: Boolean = false,
    val conditionAndTermsError: Boolean = false,
    val isServiceRentalError: Boolean = false,
    val focusError: Boolean = false,
)
