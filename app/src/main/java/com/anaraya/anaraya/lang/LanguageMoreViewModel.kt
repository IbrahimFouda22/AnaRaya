package com.anaraya.anaraya.lang

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LanguageMoreViewModel @Inject constructor() : ViewModel() {

    private val _languageMoreUiState = MutableStateFlow(LanguageMoreUiState())
    val languageMoreUiState = _languageMoreUiState.asStateFlow()

    fun changeLanguage(language: String) {
        _languageMoreUiState.update {
            it.copy(
                lang = language
            )
        }
    }

}