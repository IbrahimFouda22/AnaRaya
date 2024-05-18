package com.anaraya.anaraya.authentication


data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val productsInBasket: Int = 0,
    val contactNumber: String? = null,
    val auth: Boolean = false,
    val isSucceedSignUp: Boolean = true,
    val messageSignUp: String? = "",
    val isSucceedSignIn: Boolean = true,
    val messageSignIn: String? = "",
    val isSucceedForgetPass: Boolean = false,
    val messageForgetPass: String? = null,
    val messageCode: String? = null,
    val isSucceedCheckCode: Boolean = false,
    val messageCheckCode: String? = null,
    val isSucceedResetPass: Boolean = false,
    val messageResetPass: String? = null,
    val signInRayaIdError: Boolean = false,
    val signInNationalIdError: Boolean = false,
    val signInPasswordError: Boolean = false,
    val signInPasswordErrorMsg: String = "",
    val signUpRayaIdError: Boolean = false,
    val signUpNationalIdError: Boolean = false,
    val signUpNameError: Boolean = false,
    val signUpEmailError: Boolean = false,
    val signUpEmailErrorMsg: String = "",
    val signUpPhoneError: Boolean = false,
    val signUpPasswordError: Boolean = false,
    val signUpPasswordErrorMsg: String = "",
    val resetRayaIdError: Boolean = false,
    val resetNationalIdError: Boolean = false,
    val resetVerificationError: Boolean = false,
    val resetNewPassError: Boolean = false,
    val restPasswordErrorMsg: String = "",
    val navigateToAddAddress: Boolean = false,
    val addressUiStateData: AddressUiStateData? = null,
)

data class AddressUiStateData(
    val addressLabel: String,
    val governorate: String,
    val district: String,
    val address: String,
    val street: String,
    val building: String,
    val landmark: String,
)