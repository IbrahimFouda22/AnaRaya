package com.anaraya.anaraya.screens.services

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor() :
    ViewModel() {

    private val _servicesUiState = MutableStateFlow(ServicesUiState())
    val servicesUiState = _servicesUiState as StateFlow<ServicesUiState>

    fun navigateToItems() {
        _servicesUiState.update {
            it.copy(
                navigateToItems = true
            )
        }
    }

    fun navigateToItemsDone() {
        _servicesUiState.update {
            it.copy(
                navigateToItems = false
            )
        }
    }

    fun navigateToService() {
        _servicesUiState.update {
            it.copy(
                navigateToService = true
            )
        }
    }

    fun navigateToServiceDone() {
        _servicesUiState.update {
            it.copy(
                navigateToService = false
            )
        }
    }

}