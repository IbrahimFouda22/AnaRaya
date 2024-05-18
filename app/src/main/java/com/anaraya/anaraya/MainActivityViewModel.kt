package com.anaraya.anaraya

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val _homeState = MutableStateFlow(MainActivityUiState())
    val homeState = _homeState as StateFlow<MainActivityUiState>


    fun setError(error: String?) {
        _homeState.update {
            MainActivityUiState(
                error = error
            )
        }
    }

    fun reloadClick() {
        _homeState.update {
            MainActivityUiState(
                error = null,
                reloadClick = true
            )
        }
    }

    fun reloadClickDone() {
        _homeState.update {
            MainActivityUiState(
                error = null,
                reloadClick = false
            )
        }
    }


}