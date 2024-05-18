package com.anaraya.anaraya.home.more.feedback.question1

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class Question1ViewModel @Inject constructor() : ViewModel() {

    private val _question1UiState = MutableStateFlow(Question1UiState())
    val question1UiState = _question1UiState as StateFlow<Question1UiState>

    fun setAngry() {
        _question1UiState.update {
            if (it.angry) {
                it.copy(angry = false,rate = 0)
            } else {
                Question1UiState(
                    angry = true,
                    sad = false,
                    normal = false,
                    satisfy = false,
                    happy = false,
                    rate = 0
                )
            }

        }
    }

    fun setSad() {
        _question1UiState.update {
            if (it.sad) {
                it.copy(sad = false,rate = 0)
            }else{
                Question1UiState(
                    angry = false,
                    sad = true,
                    normal = false,
                    satisfy = false,
                    happy = false,
                    rate = 1
                )
            }
        }
    }

    fun setNormal() {
        _question1UiState.update {
            if (it.normal) {
                it.copy(normal = false,rate = 0)
            }else{
                Question1UiState(
                    angry = false,
                    sad = false,
                    normal = true,
                    satisfy = false,
                    happy = false,
                    rate = 2
                )
            }
        }
    }

    fun setSatisfy() {
        _question1UiState.update {
            if (it.satisfy) {
                it.copy(satisfy = false,rate = 0)
            }else{
                Question1UiState(
                    angry = false,
                    sad = false,
                    normal = false,
                    satisfy = true,
                    happy = false,
                    rate = 3
                )
            }
        }
    }

    fun setHappy() {
        _question1UiState.update {
            if (it.happy) {
                it.copy(happy = false,rate = 0)
            }else{
                Question1UiState(
                    angry = false,
                    sad = false,
                    normal = false,
                    satisfy = false,
                    happy = true,
                    rate = 4
                )
            }
        }
    }
}