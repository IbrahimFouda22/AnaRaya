package com.anaraya.anaraya.home.more.help.help_details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.HelpDetails
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageHelpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpDetailsViewModel @Inject constructor(
    private val manageHelpUseCase: ManageHelpUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {
    private val _helpUiState = MutableStateFlow(HelpDetailsUiState())
    val helpUiState = _helpUiState.asStateFlow()

    fun getAllData(helpId: Int) {
        getHelpDetails(helpId)
    }

    private fun getHelpDetails(helpId: Int) {
        _helpUiState.update {
            HelpDetailsUiState(
                isLoading = true, error = null, helpUiStateList = emptyList()
            )
        }

        try {
            viewModelScope.launch {
                onGetHelpDetailsSuccess(manageHelpUseCase.getHelpDetails(helpId))
            }
        } catch (e: NoInternetException) {
            onGetHelpDetailsFailure(context.getString(R.string.no_internet))
        } catch (e: Exception) {
            onGetHelpDetailsFailure(e.message)
        }
    }

    private fun onGetHelpDetailsSuccess(response: HelpDetails) {
        _helpUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                helpUiStateList = response.data.map { data ->
                    HelpDetailsUiStateData(
                        data.problem, data.answer
                    )
                }
            )
        }
    }

    private fun onGetHelpDetailsFailure(response: String?) {
        _helpUiState.update {
            it.copy(
                isLoading = false,
                error = response,
                helpUiStateList = emptyList()
            )
        }
    }

}