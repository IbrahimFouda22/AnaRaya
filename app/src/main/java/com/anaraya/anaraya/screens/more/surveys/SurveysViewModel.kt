package com.anaraya.anaraya.screens.more.surveys

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.Surveys
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveysViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageSurveysUseCase: ManageSurveysUseCase
) : ViewModel() {
    private val _surveysUiState = MutableStateFlow(SurveysUiState())
    val surveysUiState = _surveysUiState.asStateFlow()

    init {
        getSurveys()
    }

    fun getAllData(){
        getSurveys()
    }
    private fun getSurveys() {
        _surveysUiState.update {
            SurveysUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetSurveysSuccess(manageSurveysUseCase.getAllSurveys())
            } catch (e: NoInternetException) {
                onGetSurveysFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetSurveysFailure(e.message.toString())
            }
        }
    }

    private fun onGetSurveysSuccess(response: Surveys) {
        _surveysUiState.update {
            SurveysUiState(
                isLoading = false,
                error = null,
                surveys = response.data.map {
                    it.toUiState()
                }
            )
        }
    }

    private fun onGetSurveysFailure(error: String) {
        _surveysUiState.update {
            SurveysUiState(
                isLoading = false,
                error = error,
            )
        }
    }
}