package com.anaraya.anaraya.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TokenRefreshState {
    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    fun setDeleted(deleted: Boolean) {
        _isDeleted.value = deleted
    }
}