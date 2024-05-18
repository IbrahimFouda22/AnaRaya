package com.anaraya.anaraya.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.Auth
import com.anaraya.domain.entity.CheckAuth
import com.anaraya.domain.entity.ContactNumber
import com.anaraya.domain.entity.ResetChangePass
import com.anaraya.domain.exception.EmailInValidFormatException
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.exception.PasswordInValidFormatException
import com.anaraya.domain.usecase.ManageAuthUseCase
import com.anaraya.domain.usecase.ManageContactNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: ManageAuthUseCase,
    private val manageContactNumberUseCase: ManageContactNumberUseCase,
    private val sharedPreferences: SharedPreferences,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {

    private val _stateViewPager = MutableStateFlow(0)
    val stateViewPager = _stateViewPager as StateFlow<Int>

    private val _statePage = MutableStateFlow(0)
    val statePage = _statePage as StateFlow<Int>

    private val _stateSignIn = MutableStateFlow(1)
    val stateSignIn = _stateSignIn as StateFlow<Int>

    private val _stateSignUp = MutableStateFlow(1)
    val stateSignUp = _stateSignUp as StateFlow<Int>

    private val _stateResetPass = MutableStateFlow(1)
    val stateResetPass = _stateResetPass as StateFlow<Int>

    private val _signInResponse = MutableStateFlow(AuthUiState())
    val signInResponse = _signInResponse as StateFlow<AuthUiState>

    private val _signUpResponse = MutableStateFlow(AuthUiState())
    val signUpResponse = _signUpResponse as StateFlow<AuthUiState>


    private val _resetPassResponse = MutableStateFlow(AuthUiState())
    val resetPassResponse = _resetPassResponse as StateFlow<AuthUiState>

//    private val _authState = MutableStateFlow(AuthUiState())
//    val authState = _authState as StateFlow<AuthUiState>

    init {
        getContactNumber()

    }

    fun setStateViewPagerUpdate(num: Int) {
        _stateViewPager.update {
            num
        }
    }

    fun setStateSignInUpdate(num: Int) {
        _stateSignIn.update {
            num
        }
    }

    fun setStateSignUpUpdate(num: Int) {
        _stateSignUp.update {
            num
        }
    }

    fun setStateResetPassUpdate(num: Int) {
        _stateResetPass.update {
            num
        }
    }

    fun setStatePage(num: Int) {
        _statePage.update {
            num
        }
        if (num == 0)
            _stateSignIn.update {
                1
            }
        else {
            if (sharedPreferences.getBoolean(context.getString(R.string.signupstate), false)) {
                _stateSignUp.update {
                    2
                }
                sharedPreferences.edit().putBoolean(context.getString(R.string.signupstate), false)
                    .apply()
            } else {
                _stateSignUp.update {
                    1
                }
            }
        }
    }

    fun resetSignInResponse() {
        _signInResponse.update {
            AuthUiState(
                error = null
            )
        }
    }

    fun resetResetPassResponse() {
        _resetPassResponse.update {
            AuthUiState(
                error = null
            )
        }
    }

    fun signIn(rayaId: String?, national: String?, password: String?) {
        _signInResponse.update {
            AuthUiState(error = null, isLoading = true)
        }
        viewModelScope.launch {
            try {
                onSignInSuccess(authUseCase.signIn(rayaId, national, password))
            } catch (e: EmptyDataException) {
                onSignInFailure(e.message)
            } catch (e: PasswordInValidFormatException) {
                onSignInFailure(e.message)
            } catch (e: NoInternetException) {
                onSignInFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onSignInFailure(e.message)
            }
        }
    }

    private fun onSignInSuccess(response: Auth) {
        _signInResponse.update {
            AuthUiState(
                isLoading = false,
                token = response.token,
                isSucceedSignIn = response.isSucceed,
                messageSignIn = response.message,
                productsInBasket = response.productsInBasket,
                error = null
            )
        }
    }

    private fun onSignInFailure(error: String?) {
        when (error) {
            context.getString(R.string.raya_id_is_empty) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInRayaIdError = true
                    )
                }
            }

            context.getString(R.string.national_id_is_empty) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInNationalIdError = true
                    )
                }
            }

            context.getString(R.string.password_format_counter) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInPasswordError = true,
                        signInPasswordErrorMsg = context.getString(R.string.password_format_counter_msg)
                    )
                }
            }

            context.getString(R.string.password_format_upper) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInPasswordError = true,
                        signInPasswordErrorMsg = context.getString(R.string.password_format_upper_msg)
                    )
                }
            }

            context.getString(R.string.password_format_lower) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInPasswordError = true,
                        signInPasswordErrorMsg = context.getString(R.string.password_format_lower_msg)
                    )
                }
            }

            context.getString(R.string.password_format_digit) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInPasswordError = true,
                        signInPasswordErrorMsg = context.getString(R.string.password_format_digit_msg)
                    )
                }
            }

            context.getString(R.string.password_format_special) -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signInPasswordError = true,
                        signInPasswordErrorMsg = context.getString(R.string.password_format_special_msg)
                    )
                }
            }

            else -> {
                _signInResponse.update {
                    AuthUiState(
                        isLoading = false,
                        error = error,
                    )
                }
            }
        }
    }


    fun validateRayaNationalId(rayaId: String?, national: String?): Boolean {
        _signUpResponse.update {
            AuthUiState(
                error = null
            )
        }
        _resetPassResponse.update {
            AuthUiState(
                error = null
            )
        }
        return try {
            authUseCase.validateSignUpId(rayaId, national)
            true
        } catch (e: EmptyDataException) {
            onValidateRayaNationalIdFailure(e.message)
            false
        }
    }

    private fun onValidateRayaNationalIdFailure(error: String?) {
        when (error) {
            context.getString(R.string.raya_id_is_empty) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpRayaIdError = true,
                    )
                }
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        resetRayaIdError = true
                    )
                }
            }

            context.getString(R.string.national_id_is_empty) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpNationalIdError = true,
                    )
                }
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        resetNationalIdError = true
                    )
                }
            }
        }
    }

    fun checkAuth(rayaId: String, nationalId: String) {
        _signUpResponse.update {
            AuthUiState(
                isLoading = true,
                auth = false
            )
        }
        viewModelScope.launch {
            try {
                onCheckAuthSuccess(authUseCase.checkAuth(rayaId, nationalId))
            } catch (e: NoInternetException) {
                onCheckAuthFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onCheckAuthFailure(e.message.toString())
            }
        }
    }

    private fun onCheckAuthSuccess(checkAuth: CheckAuth) {
        _signUpResponse.update {
            AuthUiState(
                isLoading = false,
                auth = checkAuth.isSucceed,

                error = null
            )
        }
    }

    private fun onCheckAuthFailure(error: String) {
        _signUpResponse.update {
            AuthUiState(
                isLoading = false,
                auth = false,
                error = error
            )
        }
    }

    fun getContactNumber() {
        viewModelScope.launch {
            try {
                onGetContactNumberSuccess(manageContactNumberUseCase.getContactNumber())
            } catch (e: NoInternetException) {
                onGetContactNumberFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetContactNumberFailure(e.message.toString())
            }
        }
    }

    private fun onGetContactNumberSuccess(response: ContactNumber) {
        _signInResponse.update {
            AuthUiState(
                contactNumber = response.data
            )
        }
    }

    private fun onGetContactNumberFailure(error: String) {
        _signUpResponse.update {
            AuthUiState(
                contactNumber = null,
                error = error
            )
        }
    }

    fun signUp(
        rayaId: String?,
        national: String?,
        name: String?,
        email: String?,
        password: String?,
        phone: String?,
        dateOfBirth: String?,
        gender: Int?,
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ) {
        _signUpResponse.update {
            AuthUiState(isLoading = true, error = null)
        }
        viewModelScope.launch {
            try {
                onSignUpSuccess(
                    authUseCase.signUp(
                        rayaId,
                        national,
                        name,
                        email,
                        password,
                        phone,
                        dateOfBirth,
                        gender,
                        addressLabel,
                        governorate,
                        district,
                        address,
                        street,
                        building,
                        landmark
                    )
                )
            } catch (e: EmptyDataException) {
                onSignUpFailure(e.message)
            } catch (e: EmailInValidFormatException) {
                onSignUpFailure(e.message)
            } catch (e: PasswordInValidFormatException) {
                onSignUpFailure(e.message)
            } catch (e: NoInternetException) {
                onSignUpFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onSignUpFailure(e.message)
            }
        }
    }

    private fun onSignUpSuccess(auth: Auth) {
        _signUpResponse.update {
            AuthUiState(
                isLoading = false,
                token = auth.token,
                isSucceedSignUp = auth.isSucceed,
                messageSignUp = auth.message,
                error = null
            )
        }
    }

    private fun onSignUpFailure(error: String?) {
        when (error) {
            context.getString(R.string.name_is_empty) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpNameError = true
                    )
                }
            }

            context.getString(R.string.phone_is_empty) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpPhoneError = true
                    )
                }
            }

            context.getString(R.string.email_invalid_format) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpEmailError = true,
                        signUpEmailErrorMsg = context.getString(R.string.email_invalid_format)
                    )
                }
            }

            context.getString(R.string.password_format_counter) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpPasswordError = true,
                        signUpPasswordErrorMsg = context.getString(R.string.password_format_counter_msg)
                    )
                }
            }

            context.getString(R.string.password_format_upper) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpPasswordError = true,
                        signUpPasswordErrorMsg = context.getString(R.string.password_format_upper_msg)
                    )
                }
            }

            context.getString(R.string.password_format_lower) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpPasswordError = true,
                        signUpPasswordErrorMsg = context.getString(R.string.password_format_lower_msg)
                    )
                }
            }

            context.getString(R.string.password_format_digit) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpPasswordError = true,
                        signUpPasswordErrorMsg = context.getString(R.string.password_format_digit_msg)
                    )
                }
            }

            context.getString(R.string.password_format_special) -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        signUpPasswordError = true,
                        signUpPasswordErrorMsg = context.getString(R.string.password_format_special_msg)
                    )
                }
            }

            else -> {
                _signUpResponse.update {
                    AuthUiState(
                        isLoading = false,
                        error = error,
                    )
                }
            }
        }
    }

    fun forgetPass(
        rayaId: String,
        national: String
    ) {
        _resetPassResponse.update {
            AuthUiState(isLoading = true, error = null)
        }
        viewModelScope.launch {
            try {
                onForgetPassSuccess(
                    authUseCase.forgetPass(
                        rayaId,
                        national,
                    )
                )
            } catch (e: NoInternetException) {
                onForgetPassFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onForgetPassFailure(e.message)
            }
        }
    }

    private fun onForgetPassSuccess(response: ResetChangePass) {
        _resetPassResponse.update {
            AuthUiState(
                isLoading = false,
                isSucceedForgetPass = response.isSucceed,
                messageForgetPass = response.message,
                messageCode = response.data,
                error = null
            )
        }
    }

    private fun onForgetPassFailure(error: String?) {
        _resetPassResponse.update {
            AuthUiState(
                isLoading = false,
                isSucceedForgetPass = false,
                error = error,
                messageForgetPass = null
            )
        }
    }

    fun forgetPassCheckPass(
        rayaId: String?,
        national: String?,
        code: String?,
    ) {
        _resetPassResponse.update {
            AuthUiState(isLoading = true, error = null)
        }
        viewModelScope.launch {
            try {
                onForgetPassCheckPassSuccess(
                    authUseCase.forgetPassCheckCode(
                        rayaId,
                        national,
                        code
                    )
                )
            } catch (e: EmptyDataException) {
                onForgetPassCheckPassFailure(context.getString(R.string.no_internet))
            } catch (e: NoInternetException) {
                onForgetPassCheckPassFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onForgetPassCheckPassFailure(e.message)
            }
        }
    }

    private fun onForgetPassCheckPassSuccess(response: ResetChangePass) {
        _resetPassResponse.update {
            AuthUiState(
                isLoading = false,
                isSucceedCheckCode = response.isSucceed,
                messageCheckCode = response.message,
                error = null
            )
        }
    }

    private fun onForgetPassCheckPassFailure(error: String?) {
        when (error) {
            context.getString(R.string.raya_id_is_empty) -> {
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        resetRayaIdError = true,
                    )
                }
            }

            context.getString(R.string.national_id_is_empty) -> {
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        resetNationalIdError = true
                    )
                }
            }

            context.getString(R.string.verification_is_empty) -> {
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        resetVerificationError = true
                    )
                }
            }

            else -> {
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        error = error,
                        messageCheckCode = null,
                        messageCode = null
                    )
                }
            }
        }
    }

    fun resetPass(
        rayaId: String,
        national: String,
        code: String,
        newPass: String?
    ) {
        _resetPassResponse.update {
            AuthUiState(isLoading = true, error = null)
        }
        viewModelScope.launch {
            try {
                onResetPassSuccess(
                    authUseCase.resetPass(
                        rayaId,
                        national,
                        code,
                        newPass
                    )
                )
            } catch (e: EmptyDataException) {
                onResetPassFailure(context.getString(R.string.no_internet))
            } catch (e: NoInternetException) {
                onResetPassFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onResetPassFailure(e.message)
            }
        }
    }

    private fun onResetPassSuccess(response: ResetChangePass) {
        _resetPassResponse.update {
            AuthUiState(
                isLoading = false,
                isSucceedResetPass = response.isSucceed,
                messageResetPass = response.message,
                error = null
            )
        }
    }

    private fun onResetPassFailure(error: String?) {
        when (error) {
            context.getString(R.string.new_password_is_empty) -> {
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        resetNewPassError = true
                    )
                }
            }

            else -> {
                _resetPassResponse.update {
                    AuthUiState(
                        isLoading = false,
                        error = error,
                        messageResetPass = null
                    )
                }
            }
        }
    }

    fun resetStates() {
        _resetPassResponse.update {
            AuthUiState()
        }
    }

    fun resetContact() {
        _signInResponse.update {
            it.copy(contactNumber = null)
        }
    }

    fun navigateToAddAddress() {
        _signUpResponse.update {
            it.copy(navigateToAddAddress = true)
        }
    }

    fun navigateToAddAddressDone() {
        _signUpResponse.update {
            it.copy(navigateToAddAddress = false)
        }
    }

    fun setAddress(
        addressUiStateData: AddressUiStateData?
    ) {
        _signUpResponse.update {
            it.copy(
                addressUiStateData = addressUiStateData
            )
        }
    }
}