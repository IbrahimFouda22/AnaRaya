package com.anaraya.anaraya.screens.more.terms_condition

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
class TermsAndConditionViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageInfoUseCase: ManageInfoUseCase
) : ViewModel() {
    private val _termsAndConditionUiState = MutableStateFlow(TermsAndConditionUiState())
    val termsAndConditionUiState = _termsAndConditionUiState.asStateFlow()

    init {
        getTermsAndConditions()
    }

    fun getAllData(){
        getTermsAndConditions()
    }
    private fun getTermsAndConditions() {
        _termsAndConditionUiState.update {
            TermsAndConditionUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetTermsAndConditionsSuccess(manageInfoUseCase.getTermsAndConditions())
            } catch (e: NoInternetException) {
                onGetTermsAndConditionsFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetTermsAndConditionsFailure(e.message.toString())
            }
        }
    }

    private fun onGetTermsAndConditionsSuccess(response: BaseResponse) {
        _termsAndConditionUiState.update {
            TermsAndConditionUiState(
                isLoading = false,
                error = null,
                data = response.data,
            )
        }
    }

    private fun onGetTermsAndConditionsFailure(error: String) {
        _termsAndConditionUiState.update {
            TermsAndConditionUiState(
                isLoading = false,
                error = error,
            )
        }
    }
}