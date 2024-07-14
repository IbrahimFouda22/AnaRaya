package com.anaraya.anaraya.screens.more.my_information.edit_gender

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
class EditGenderViewModel @AssistedInject constructor(
    private val manageProfileUseCase: ManageProfileUseCase,
    @Assisted("value") private var value: Int,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _editInfoUiState = MutableStateFlow(EditGenderUiState())
    val editInfoUiState = _editInfoUiState as StateFlow<EditGenderUiState>

    private val _valueState = MutableStateFlow(value)
    val valueState = _valueState.asStateFlow()


    fun updateVisibility() {
        if (valueState.value == value)
            _editInfoUiState.update {
                it.copy(saveIsVisible = false, msgUpdate = null)
            }
        else
            _editInfoUiState.update {
                it.copy(saveIsVisible = true, msgUpdate = null)
            }
    }

    fun updateGender(gender:Int){
        _valueState.value = gender
        updateVisibility()
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
                onUpdateSuccess(manageProfileUseCase.updateGender(valueState.value))
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
        value = valueState.value
        updateVisibility()
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
    interface EditGenderAssistedFactory {
        fun create(
            @Assisted("value") value: Int
        ): EditGenderViewModel
    }

    companion object {
        fun create(
            assistedFactory: EditGenderAssistedFactory,
            value: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(value) as T
                }
            }
        }
    }


}