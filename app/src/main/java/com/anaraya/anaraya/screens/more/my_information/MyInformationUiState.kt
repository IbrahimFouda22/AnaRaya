package com.anaraya.anaraya.screens.more.my_information
import com.anaraya.anaraya.screens.shared_data.ProfileUiState
data class MyInfoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val profileUiState: ProfileUiState? = null,
    val navigateToAddresses: Boolean = false,
    val navigateToName: Boolean = false,
    val navigateToEmail: Boolean = false,
    val navigateToPhone: Boolean = false,
    val navigateToVerifyPhone: Boolean = false,
    val navigateToDOB: Boolean = false,
    val navigateToGender: Boolean = false,
    val navigateToChangePass: Boolean = false,
    val isSendOtpSucceed: Boolean = false,
    val msgSendOtp: String? = null,
    val visibleVerifyPhone:Boolean=false
)
