package com.anaraya.anaraya.home.more.family.family_referrals

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.Referrals
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
class FamilyReferralsViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageFamilyUseCase: ManageFamilyUseCase
) : ViewModel() {
    private val _familyReferralsUiState = MutableStateFlow(FamilyReferralsUiState())
    val familyReferralsUiState = _familyReferralsUiState.asStateFlow()

    init {
        getAllFamilyReferrals()
    }

    fun getAllData() {
        getAllFamilyReferrals()
    }

    private fun getAllFamilyReferrals() {
        _familyReferralsUiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAllFamilyReferralsSuccess(manageFamilyUseCase.getAllReferrals())
            } catch (e: NoInternetException) {
                onGetAllFamilyReferralsFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAllFamilyReferralsFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllFamilyReferralsSuccess(response: Referrals) {
        _familyReferralsUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                listFamilyReferrals= response.data.map { data ->
                    data.toUiState()
                }
            )
        }
    }

    private fun onGetAllFamilyReferralsFailure(error: String) {
        _familyReferralsUiState.update {
            it.copy(
                isLoading = false,
                error = error,
            )
        }
    }

}