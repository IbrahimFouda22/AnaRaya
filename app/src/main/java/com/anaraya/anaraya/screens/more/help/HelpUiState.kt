package com.anaraya.anaraya.screens.more.help

import java.io.Serializable


data class HelpUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val helpUiStateList: List<HelpUiStateData> = emptyList()
):Serializable

data class HelpUiStateData(
    val id: Int,
    val topic: String?
):Serializable