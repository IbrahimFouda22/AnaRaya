package com.anaraya.anaraya.home.schedule

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.home.cart.CartUiState
import com.anaraya.anaraya.home.cart.toUiState
import com.anaraya.domain.entity.DeliverySchedule
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageDeliveryScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel
@Inject constructor(
    private val manageDeliveryScheduleUseCase: ManageDeliveryScheduleUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {


    private val _scheduleUiState = MutableStateFlow(DeliveryScheduleUiState())
    val scheduleUiState = _scheduleUiState.asStateFlow()

//
//    private val _navigateToHome = MutableStateFlow(false)
//    val navigateToHome = _navigateToHome as StateFlow<Boolean>

    init {
        getImageDelivery()
    }

    fun getAllData(){
        getImageDelivery()
    }
    private fun getImageDelivery() {
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = true, error = null, deliveryScheduleImage = null
            )
        }
        viewModelScope.launch {
            try {
                onGetScheduleImageSuccess(manageDeliveryScheduleUseCase.getDeliverySchedule())
            } catch (e: NoInternetException) {
                onGetScheduleImageFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetScheduleImageFailure(e.message.toString())
            }
        }
    }

    private fun onGetScheduleImageSuccess(response: DeliverySchedule) {
        _scheduleUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                deliveryScheduleImage = response.data
            )
        }

    }

    private fun onGetScheduleImageFailure(error: String) {
        _scheduleUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                deliveryScheduleImage = null
            )
        }
    }

    fun navigateToBack() {
        _scheduleUiState.update {
            it.copy(
                navigateToBack = true
            )
        }
    }

    fun navigateToBackDone() {
        _scheduleUiState.update {
            it.copy(
                navigateToBack = false
            )
        }
    }
}