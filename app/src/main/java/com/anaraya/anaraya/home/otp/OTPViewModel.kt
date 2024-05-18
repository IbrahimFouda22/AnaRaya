package com.anaraya.anaraya.home.otp

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {
    private val _otpUiState = MutableStateFlow(OTPUiState())
    val otpUiState = _otpUiState as StateFlow<OTPUiState>
    fun getAllData(code: String) {
        verifyPhone(code)
    }
    fun verifyPhone(code: String) {
        _otpUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                msgUpdate = null, isSucceed = false
            )
        }
        viewModelScope.launch {
            try {
                onVerifyPhoneSuccess(manageProfileUseCase.verifyPhone(code))
            } catch (e: NoInternetException) {
                onVerifyPhoneFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onVerifyPhoneFailure(e.message.toString())
            }
        }
    }
    private fun onVerifyPhoneSuccess(response: BaseResponse) {
        _otpUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                isSucceed = response.isSucceed,
                msgUpdate = response.message
            )
        }
    }

    private fun onVerifyPhoneFailure(error: String) {
        _otpUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceed = false,
                msgUpdate = null
            )
        }
    }

}