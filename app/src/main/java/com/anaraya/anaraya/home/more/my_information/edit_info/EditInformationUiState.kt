package com.anaraya.anaraya.home.more.my_information.edit_info

data class EditInfoUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val saveIsVisible: Boolean = false,
    val error: String? = null,
    val msgUpdate: String? = null,
)
