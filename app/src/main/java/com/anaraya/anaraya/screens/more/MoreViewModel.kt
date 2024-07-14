package com.anaraya.anaraya.screens.more

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.shared_data.ProfileUiState
import com.anaraya.anaraya.screens.shared_data.toUiState
import com.anaraya.domain.entity.AddDeleteImage
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Profile
import com.anaraya.domain.entity.SurveysStatus
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageProfileUseCase
import com.anaraya.domain.usecase.ManageSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MoreViewModel @Inject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    private val manageSurveysUseCase: ManageSurveysUseCase,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    private val _moreUiState = MutableStateFlow(MoreUiState())
    val moreUiState = _moreUiState as StateFlow<MoreUiState>

    init {
        getProfile()
        getLoyaltyPoints()
        getSurveysStatus()
    }

    fun getAllData() {
        getProfile()
        getLoyaltyPoints()
        getSurveysStatus()
    }

    private fun getProfile() {
        _moreUiState.update {
            it.copy(
                isLoading = true, error = null, uploadMsg = null
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
        _moreUiState.update {
            it.copy(
                isLoading = false, error = null, profileUiState = ProfileUiState(
                    gender = response.data.gender,
                    name = response.data.name,
                    email = response.data.email,
                    phoneNumber = response.data.phoneNumber,
                    profileImage = response.data.profileImage,
                    defaultAddressUiState = response.data.userDefaultAddress?.toUiState(),
                    phoneNumberConfirmed = response.data.phoneNumberConfirmed,
                    visibilityEmail = !response.data.email.isNullOrEmpty()
                )
            )
        }
    }

    private fun onGetProfileFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = error, profileUiState = null
            )
        }
    }

    fun uploadImage(file: File) {
        _moreUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onUploadImageSuccess(manageProfileUseCase.uploadImage(file))
            } catch (e: NoInternetException) {
                onUploadImageFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onUploadImageFailure(e.message.toString())
            }
        }
    }

    private fun onUploadImageSuccess(response: AddDeleteImage) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = null, uploadMsg = response.message
            )
        }
        getProfile()
    }

    private fun onUploadImageFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = error, uploadMsg = null
            )
        }
    }

    private fun getLoyaltyPoints() {
        _moreUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetLoyaltyPointsSuccess(manageProfileUseCase.getUserLoyaltyPoints())
            } catch (e: NoInternetException) {
                onGetLoyaltyPointsFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetLoyaltyPointsFailure(e.message.toString())
            }
        }
    }

    private fun onGetLoyaltyPointsSuccess(response: BaseResponse) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = null, loyaltyPoints = response.data?.toInt() ?: 0
            )
        }
    }

    private fun onGetLoyaltyPointsFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = error, loyaltyPoints = 0
            )
        }
    }

    private fun getSurveysStatus() {
        _moreUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetSurveysStatusSuccess(manageSurveysUseCase.getSurveysStatus())
            } catch (e: NoInternetException) {
                onGetSurveysStatusFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetSurveysStatusFailure(e.message.toString())
            }
        }
    }

    private fun onGetSurveysStatusSuccess(response: SurveysStatus) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = null, showSurveys = !response.data
            )
        }
    }

    private fun onGetSurveysStatusFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false, error = error, showSurveys = false
            )
        }
    }

    fun navigateToFeedBack() {
        _moreUiState.update {
            it.copy(
                navigateToFeedBack = true
            )
        }
    }

    fun navigateToFeedBackDone() {
        _moreUiState.update {
            it.copy(
                navigateToFeedBack = false
            )
        }
    }

    fun navigateToMyInfo() {
        _moreUiState.update {
            it.copy(
                navigateToMyInfo = true
            )
        }
    }

    fun navigateToMyInfoDone() {
        _moreUiState.update {
            it.copy(
                navigateToMyInfo = false
            )
        }
    }

    fun navigateToOrders() {
        _moreUiState.update {
            it.copy(
                navigateToOrders = true
            )
        }
    }

    fun navigateToOrdersDone() {
        _moreUiState.update {
            it.copy(
                navigateToOrders = false
            )
        }
    }

    fun navigateToHelp() {
        _moreUiState.update {
            it.copy(navigateToHelp = true)
        }
    }

    fun navigateToHelpDone() {
        _moreUiState.update {
            it.copy(navigateToHelp = false)
        }
    }

    fun navigateToSchedule() {
        _moreUiState.update {
            it.copy(navigateToSchedule = true)
        }
    }

    fun navigateToScheduleDone() {
        _moreUiState.update {
            it.copy(navigateToSchedule = false)
        }
    }

    fun navigateToAboutUs() {
        _moreUiState.update {
            it.copy(navigateToAboutUs = true)
        }
    }

    fun navigateToAboutUsDone() {
        _moreUiState.update {
            it.copy(navigateToAboutUs = false)
        }
    }

    fun navigateToFamily() {
        _moreUiState.update {
            it.copy(navigateToFamily = true)
        }
    }

    fun navigateToFamilyDone() {
        _moreUiState.update {
            it.copy(navigateToFamily = false)
        }
    }

    fun navigateToTermsCondition() {
        _moreUiState.update {
            it.copy(navigateToTermsCondition = true)
        }
    }

    fun navigateToTermsConditionDone() {
        _moreUiState.update {
            it.copy(navigateToTermsCondition = false)
        }
    }
    fun navigateToLanguage() {
        _moreUiState.update {
            it.copy(navigateToLanguage = true)
        }
    }

    fun navigateToLanguageDone() {
        _moreUiState.update {
            it.copy(navigateToLanguage = false)
        }
    }
    fun navigateToPrivacyPolicy() {
        _moreUiState.update {
            it.copy(navigateToPrivacyPolicy = true)
        }
    }

    fun navigateToPrivacyPolicyDone() {
        _moreUiState.update {
            it.copy(navigateToPrivacyPolicy = false)
        }
    }

    fun navigateToSurveys() {
        _moreUiState.update {
            it.copy(navigateToSurveys = true)
        }
    }

    fun navigateToSurveysDone() {
        _moreUiState.update {
            it.copy(navigateToSurveys = false)
        }
    }

    fun sendFCMToken(token: String, enabled: Boolean, isUpdate: Boolean) {
        _moreUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isFCMSent = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isUpdate)
                    onSendFCMTokenSuccess(manageProfileUseCase.updateFCMToken(token, enabled))
                else
                    onSendFCMTokenSuccess(manageProfileUseCase.sendFCMToken(token, enabled))
            } catch (e: NoInternetException) {
                onSendFCMTokenFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onSendFCMTokenFailure(e.message.toString())
            }
        }
    }

    private fun onSendFCMTokenSuccess(response: BaseResponse) {
        _moreUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                isFCMSent = response.isSucceed,
            )
        }
    }

    private fun onSendFCMTokenFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isFCMSent = false,
            )
        }
    }
}