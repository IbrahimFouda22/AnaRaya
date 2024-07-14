package com.anaraya.anaraya.screens.more.privacy_policy

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageInfoUseCase: ManageInfoUseCase
) : ViewModel() {
    private val _privacyPolicyUiState = MutableStateFlow(PrivacyPolicyUiState())
    val privacyPolicyUiState = _privacyPolicyUiState.asStateFlow()

    init {
        getPrivacyPolicy()
    }

    fun getAllData(){
        getPrivacyPolicy()
    }
    private fun getPrivacyPolicy() {
        _privacyPolicyUiState.update {
            PrivacyPolicyUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetPrivacyPolicySuccess(manageInfoUseCase.getPrivacyPolicy())
            } catch (e: NoInternetException) {
                onGetPrivacyPolicyFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetPrivacyPolicyFailure(e.message.toString())
            }
        }
    }

    private fun onGetPrivacyPolicySuccess(response: BaseResponse) {
        _privacyPolicyUiState.update {
            PrivacyPolicyUiState(
                isLoading = false,
                error = null,
                data = response.data,
            )
        }
    }

    private fun onGetPrivacyPolicyFailure(error: String) {
        _privacyPolicyUiState.update {
            PrivacyPolicyUiState(
                isLoading = false,
                error = error,
            )
        }
    }
}