package com.anaraya.anaraya.home.more.my_information.edit_info

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.EditInfo
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageProfileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EditInformationViewModel @AssistedInject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    @Assisted("type") val type: String,
    @Assisted("value") private val value: String?,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _editInfoUiState = MutableStateFlow(EditInfoUiState())
    val editInfoUiState = _editInfoUiState as StateFlow<EditInfoUiState>

    val valueState = if (value.isNullOrEmpty()) MutableStateFlow("")else MutableStateFlow(value.toString())


    fun updateVisibility() {
        if (valueState.value == value || valueState.value.isEmpty())
            _editInfoUiState.update {
                it.copy(saveIsVisible = false)
            }
        else
            _editInfoUiState.update {
                it.copy(saveIsVisible = true)
            }
    }


    fun getAllData() {
        updateData()
    }

    fun updateData() {
        _editInfoUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                msgUpdate = null, isSucceed = false
            )
        }

        viewModelScope.launch {
            try {
                when (type) {
                    "name" -> onUpdateSuccess(manageProfileUseCase.updateName(valueState.value))
                    "email" -> onUpdateSuccess(manageProfileUseCase.updateEmail(valueState.value))
                    else -> onUpdateSuccess(manageProfileUseCase.updatePhoneNumber(valueState.value))
                }
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
                msgUpdate = response.message
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

    @AssistedFactory
    interface EditInfoAssistedFactory {
        fun create(
            @Assisted("type") type: String,
            @Assisted("value") value: String?
        ): EditInformationViewModel
    }

    companion object {
        fun create(
            assistedFactory: EditInfoAssistedFactory,
            type: String, value: String?
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(type, value) as T
                }
            }
        }
    }


}