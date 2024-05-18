package com.anaraya.anaraya.home.more.family

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Relationships
import com.anaraya.domain.exception.EmailInValidFormatException
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageFamilyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageFamilyUseCase: ManageFamilyUseCase
) : ViewModel() {
    private val _familyUiState = MutableStateFlow(FamilyUiState())
    val familyUiState = _familyUiState.asStateFlow()

    init {
        getAllRelationships()
    }

    fun getAllData() {
        getAllRelationships()
    }

    private fun getAllRelationships() {
        _familyUiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAllRelationshipsSuccess(manageFamilyUseCase.getAllRelationships())
            } catch (e: NoInternetException) {
                onGetAllRelationshipsFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllRelationshipsFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllRelationshipsSuccess(response: List<Relationships>) {
        _familyUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                listRelationships = response.map { data ->
                    data.toUiState()
                }
            )
        }
    }

    private fun onGetAllRelationshipsFailure(error: String) {
        _familyUiState.update {
            it.copy(
                isLoading = false,
                error = error,
            )
        }
    }

    fun addNewReferral(
        name: String?,
        phoneNumber: String?,
        relationshipId: Int?,
        email: String? = null
    ) {
        _familyUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                addReferralMsg = ""
            )
        }
        viewModelScope.launch {
            try {
                onAddNewReferralSuccess(
                    manageFamilyUseCase.addNewReferral(
                        name,
                        phoneNumber,
                        relationshipId,
                        email
                    )
                )
            } catch (e: EmptyDataException) {
                onAddNewReferralFailure(e.message.toString())
            } catch (e: EmailInValidFormatException) {
                onAddNewReferralFailure(e.message.toString())
            } catch (e: NoInternetException) {
                onAddNewReferralFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddNewReferralFailure(e.message.toString())
            }
        }
    }

    private fun onAddNewReferralSuccess(response: BaseResponse) {
        _familyUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                addReferralMsg = response.message
            )
        }
        resetMsg()
    }

    private fun onAddNewReferralFailure(error: String) {
        when (error) {
            context.getString(R.string.name_is_empty) -> {
                _familyUiState.update {
                    it.copy(
                        isLoading = false,
                        nameError = true
                    )
                }
            }

            context.getString(R.string.phone_is_empty) -> {
                _familyUiState.update {
                    it.copy(
                        isLoading = false,
                        nameError = false,
                        phoneError = true
                    )
                }
            }

            context.getString(R.string.relationship_is_empty) -> {
                _familyUiState.update {
                    it.copy(
                        isLoading = false,
                        nameError = false,
                        phoneError = false,
                        relationshipError = true
                    )
                }
            }

            context.getString(R.string.email_invalid_format) -> {
                _familyUiState.update {
                    it.copy(
                        isLoading = false,
                        nameError = false,
                        phoneError = false,
                        relationshipError = false,
                        emailErrorMsg = context.getString(R.string.email_invalid_format)
                    )
                }
            }

            else -> {
                _familyUiState.update {
                    it.copy(
                        isLoading = false,
                        nameError = false,
                        phoneError = false,
                        relationshipError = false,
                        emailErrorMsg = "",
                        error = error,
                    )
                }
            }
        }
    }

    private fun resetMsg() {
        _familyUiState.update {
            it.copy(
                addReferralMsg = null,
                nameError = false,
                phoneError = false,
                relationshipError = false,
                emailErrorMsg = "",
            )
        }
    }

}