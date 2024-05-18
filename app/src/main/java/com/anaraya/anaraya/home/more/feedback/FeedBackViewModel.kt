package com.anaraya.anaraya.home.more.feedback

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedBackViewModel @Inject constructor() : ViewModel() {
    private val _feedBackState = MutableStateFlow(FeedBackUiState())
    val feedBackState = _feedBackState.asStateFlow()

    fun setStateViewPager(state:Int){
        _feedBackState.update {
            it.copy(
                stateViewPager = state
            )
        }
    }
    fun resetStateViewPager(){
        _feedBackState.update {
            it.copy(
                stateViewPager = -1
            )
        }
    }
    fun setRate(rate:Int){
        _feedBackState.update {
            it.copy(
                rate = rate
            )
        }
    }
}