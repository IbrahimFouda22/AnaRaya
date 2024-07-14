package com.anaraya.anaraya.screens.more.my_information.edit_gender

data class EditGenderUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val saveIsVisible: Boolean = false,
    val error: String? = null,
    val msgUpdate: String? = null,
)
