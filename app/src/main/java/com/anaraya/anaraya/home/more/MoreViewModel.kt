package com.anaraya.anaraya.home.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.home.shared_data.ProfileUiState
import com.anaraya.anaraya.home.shared_data.toUiState
import com.anaraya.domain.entity.AddDeleteImage
import com.anaraya.domain.entity.Profile
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MoreViewModel @Inject constructor(private val manageProfileUseCase: ManageProfileUseCase) :
    ViewModel() {

    private val _moreUiState = MutableStateFlow(MoreUiState())
    val moreUiState = _moreUiState as StateFlow<MoreUiState>


    init {
        getProfile()
    }

    fun getAllData() {
        getProfile()
    }

    private fun getProfile() {
        _moreUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                uploadMsg = null
            )
        }
        viewModelScope.launch {
            try {
                onGetProfileSuccess(manageProfileUseCase.getProfileData())
            } catch (e: NoInternetException) {
                onGetProfileFailure("No Internet")
            } catch (e: Exception) {
                onGetProfileFailure(e.message.toString())
            }
        }
    }

    private fun onGetProfileSuccess(response: Profile) {
        _moreUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                profileUiState = ProfileUiState(
                    name = response.data.name,
                    email = response.data.email,
                    phoneNumber = response.data.phoneNumber,
                    profileImage = response.data.profileImage,
                    defaultAddressUiState = response.data.userDefaultAddress?.toUiState(),
                    phoneNumberConfirmed = response.data.phoneNumberConfirmed
                )
            )
        }
    }

    private fun onGetProfileFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                profileUiState = null
            )
        }
    }

    fun uploadImage(file:File) {
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
                onUploadImageFailure("No Internet")
            } catch (e: Exception) {
                onUploadImageFailure(e.message.toString())
            }
        }
    }

    private fun onUploadImageSuccess(response: AddDeleteImage) {
        _moreUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                uploadMsg = response.message
            )
        }
        getProfile()
    }

    private fun onUploadImageFailure(error: String) {
        _moreUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                uploadMsg = null
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
}