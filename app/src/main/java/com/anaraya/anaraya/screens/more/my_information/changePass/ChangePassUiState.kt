package com.anaraya.anaraya.screens.more.my_information.changePass

data class ChangePassUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val saveIsVisible: Boolean = false,
    val error: String? = null,
    val msgUpdate: String? = null,
    val currentPasswordErrorNumber: Int = 0,
    val newPasswordErrorNumber: Int = 0,
)
