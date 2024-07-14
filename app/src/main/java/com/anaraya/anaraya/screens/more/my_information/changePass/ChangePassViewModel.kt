package com.anaraya.anaraya.screens.more.my_information.changePass

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.EditInfo
import com.anaraya.domain.exception.CurrentPasswordInValidFormatException
import com.anaraya.domain.exception.NewPasswordInValidFormatException
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
class ChangePassViewModel @Inject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) :
    ViewModel() {

    private val _editInfoUiState = MutableStateFlow(ChangePassUiState())
    val editInfoUiState = _editInfoUiState as StateFlow<ChangePassUiState>

    fun getAllData(currentPass: String, newPass: String) {
        updateData(currentPass, newPass)
    }

    fun updateData(currentPass: String, newPass: String) {
        _editInfoUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                msgUpdate = null, isSucceed = false
            )
        }

        viewModelScope.launch {
            try {
                onUpdateSuccess(manageProfileUseCase.changePassword(currentPass, newPass))
            } catch (e: CurrentPasswordInValidFormatException) {
                onUpdateCurrentFailure(e.message.toString())
            } catch (e: NewPasswordInValidFormatException) {
                onUpdateNewFailure(e.message.toString())
            } catch (e: NoInternetException) {
                onUpdateFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onUpdateFailure(e.message.toString())
            }
        }
    }

    private fun onUpdateSuccess(response: EditInfo) {
        _editInfoUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                isSucceed = true,
                msgUpdate = response.message,
            )
        }
    }

    private fun onUpdateFailure(error: String) {
        _editInfoUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceed = false,
                msgUpdate = null
            )
        }
    }

    private fun onUpdateCurrentFailure(error: String) {
        when (error) {
            context.getString(R.string.password_format_counter) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        newPasswordErrorNumber = 0,
                        currentPasswordErrorNumber = 1
                    )
                }
            }

            context.getString(R.string.password_format_upper) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        newPasswordErrorNumber = 0,
                        currentPasswordErrorNumber = 2
                    )
                }
            }

            context.getString(R.string.password_format_lower) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        newPasswordErrorNumber = 0,
                        currentPasswordErrorNumber = 3
                    )
                }
            }

            context.getString(R.string.password_format_digit) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        newPasswordErrorNumber = 0,
                        currentPasswordErrorNumber = 4
                    )
                }
            }

            context.getString(R.string.password_format_special) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        newPasswordErrorNumber = 0,
                        currentPasswordErrorNumber = 5
                    )
                }
            }
        }
    }

    private fun onUpdateNewFailure(error: String) {
        when (error) {
            context.getString(R.string.password_format_counter) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        currentPasswordErrorNumber = 0,
                        newPasswordErrorNumber = 1
                    )
                }
            }

            context.getString(R.string.password_format_upper) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        currentPasswordErrorNumber = 0,
                        newPasswordErrorNumber = 2
                    )
                }
            }

            context.getString(R.string.password_format_lower) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        currentPasswordErrorNumber = 0,
                        newPasswordErrorNumber = 3
                    )
                }
            }

            context.getString(R.string.password_format_digit) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        currentPasswordErrorNumber = 0,
                        newPasswordErrorNumber = 4
                    )
                }
            }

            context.getString(R.string.password_format_special) -> {
                _editInfoUiState.update {
                    it.copy(
                        isLoading = false,
                        currentPasswordErrorNumber = 0,
                        newPasswordErrorNumber = 5
                    )
                }
            }
        }
    }

}