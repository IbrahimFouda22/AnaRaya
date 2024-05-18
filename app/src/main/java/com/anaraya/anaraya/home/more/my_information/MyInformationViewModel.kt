package com.anaraya.anaraya.home.more.my_information

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.home.shared_data.AddressUiState
import com.anaraya.anaraya.home.shared_data.ProfileUiState
import com.anaraya.anaraya.home.shared_data.toUiState
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Profile
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
class MyInformationViewModel @Inject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {
    private val _myInfoUiState = MutableStateFlow(MyInfoUiState())
    val myInfoUiState = _myInfoUiState as StateFlow<MyInfoUiState>
    init {
        getProfile()
    }
    fun getAllData() {
        getProfile()
    }
    private fun getProfile() {
        _myInfoUiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetProfileSuccess(manageProfileUseCase.getProfileData())
            } catch (e: NoInternetException) {
                onGetProfileFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetProfileFailure(e.message.toString())
            }
        }
    }
    private fun onGetProfileSuccess(response: Profile) {
        _myInfoUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                visibleVerifyPhone = !response.data.phoneNumberConfirmed,
                profileUiState = ProfileUiState(
                    name = response.data.name,
                    email = response.data.email,
                    phoneNumber = response.data.phoneNumber,
                    phoneNumberConfirmed = response.data.phoneNumberConfirmed,
                    defaultAddressUiState = if (response.data.userDefaultAddress == null && response.data.companyDefaultAddress == null) null
                    else if (response.data.userDefaultAddress != null) response.data.userDefaultAddress?.toUiState()
                    else AddressUiState(
                        id = response.data.companyDefaultAddress!!.id,
                        addressLabel = response.data.companyDefaultAddress!!.companyName,
                        address = response.data.companyDefaultAddress!!.companyName,
                        governorate = response.data.companyDefaultAddress!!.governorate,
                        district = "",
                        landmark = "",
                        building = "",
                        street = "",
                    ),
                )
            )
        }
    }
    private fun onGetProfileFailure(error: String) {
        _myInfoUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                profileUiState = null
            )
        }
    }
    fun sendPhoneOtp() {
        _myInfoUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                msgSendOtp = null, isSendOtpSucceed = false
            )
        }

        viewModelScope.launch {
            try {
                onSendPhoneOtpSuccess(manageProfileUseCase.sendPhoneOtp())
            } catch (e: NoInternetException) {
                onSendPhoneOtpFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onSendPhoneOtpFailure(e.message.toString())
            }
        }
    }

    private fun onSendPhoneOtpSuccess(response: BaseResponse) {
        _myInfoUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                isSendOtpSucceed = true,
                msgSendOtp = response.message
            )
        }
    }

    private fun onSendPhoneOtpFailure(error: String) {
        _myInfoUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSendOtpSucceed = false,
                msgSendOtp = null
            )
        }
    }
    fun navigateToAddresses() {
        _myInfoUiState.update {
            it.copy(
                navigateToAddresses = true
            )
        }
    }
    fun navigateToAddressesDone() {
        _myInfoUiState.update {
            it.copy(
                navigateToAddresses = false
            )
        }
    }
    fun navigateToName() {
        _myInfoUiState.update {
            it.copy(
                navigateToName = true,
            )
        }
    }
    fun navigateToNameDone() {
        _myInfoUiState.update {
            it.copy(
                navigateToName = false,
            )
        }
    }
    fun navigateToEmail() {
        _myInfoUiState.update {
            it.copy(
                navigateToEmail = true,
            )
        }
    }
    fun navigateToEmailDone() {
        _myInfoUiState.update {
            it.copy(
                navigateToEmail = false,
            )
        }
    }
    fun navigateToPhone() {
        _myInfoUiState.update {
            it.copy(
                navigateToPhone = true,
            )
        }
    }
    fun navigateToPhoneDone() {
        _myInfoUiState.update {
            it.copy(
                navigateToPhone = false,
            )
        }
    }
    fun navigateToVerifyPhone() {
        _myInfoUiState.update {
            it.copy(
                navigateToVerifyPhone = true,
            )
        }
    }
    fun navigateToVerifyPhoneDone() {
        _myInfoUiState.update {
            it.copy(
                navigateToVerifyPhone = false,
            )
        }
    }
    fun navigateToChangePass() {
        _myInfoUiState.update {
            it.copy(
                navigateToChangePass = true
            )
        }
    }
    fun navigateToChangePassDone() {
        _myInfoUiState.update {
            it.copy(
                navigateToChangePass = false
            )
        }
    }
    fun resetMsg(){
        _myInfoUiState.update {
            it.copy(
                isSendOtpSucceed = false,
                msgSendOtp = null
            )
        }
    }
}