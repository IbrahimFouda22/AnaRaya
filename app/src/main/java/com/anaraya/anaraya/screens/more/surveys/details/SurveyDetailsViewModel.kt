package com.anaraya.anaraya.screens.more.surveys.details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Survey
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageSurveysUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class SurveyDetailsViewModel @SuppressLint("StaticFieldLeak")
@AssistedInject constructor(
    @Assisted val surveyId: Int,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageSurveysUseCase: ManageSurveysUseCase,
) : ViewModel() {
    private val _surveysUiState = MutableStateFlow(SurveyDetailsUiState())
    val surveysUiState = _surveysUiState.asStateFlow()

    init {
        getSurvey()
    }

    fun getAllData() {
        getSurvey()
    }

    private fun getSurvey() {
        _surveysUiState.update {
            SurveyDetailsUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetSurveysSuccess(manageSurveysUseCase.getSurvey(surveyId))
            } catch (e: NoInternetException) {
                onGetSurveysFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetSurveysFailure(e.message.toString())
            }
        }
    }

    private fun onGetSurveysSuccess(response: Survey) {
        _surveysUiState.update {
            SurveyDetailsUiState(
                isLoading = false,
                error = null,
                survey = response.toUiState(),
                surveyTitle = response.data.title
            )
        }
    }

    private fun onGetSurveysFailure(error: String) {
        _surveysUiState.update {
            SurveyDetailsUiState(
                isLoading = false,
                error = error,
            )
        }
    }

    fun submitSurvey(surveyBodyUiState: SurveyBodyUiState) {
        _surveysUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSucceedSubmitSurvey = false,
                msgSubmitSurvey = null
            )
        }
        viewModelScope.launch {
            try {
                onSubmitSurveySuccess(manageSurveysUseCase.submitSurvey(surveyBodyUiState.toEntity()))
            } catch (e: NoInternetException) {
                onSubmitSurveyFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onSubmitSurveyFailure(e.message.toString())
            }
        }
    }

    private fun onSubmitSurveySuccess(response: BaseResponse) {
        _surveysUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                isSucceedSubmitSurvey = response.isSucceed,
                msgSubmitSurvey = response.message
            )
        }
    }

    private fun onSubmitSurveyFailure(error: String) {
        _surveysUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceedSubmitSurvey = false,
            )
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(surveyId: Int): SurveyDetailsViewModel
    }

    companion object {
        fun createFactory(
            surveyId: Int, assistedFactory: AssistedFactory,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(surveyId) as T
                }
            }
        }
    }
}