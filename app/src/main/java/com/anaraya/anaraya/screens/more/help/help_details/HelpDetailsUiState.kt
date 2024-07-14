package com.anaraya.anaraya.screens.more.help.help_details


data class HelpDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val helpUiStateList: List<HelpDetailsUiStateData> = emptyList()
)

data class HelpDetailsUiStateData(
    val problem: String,
    val answer: String?
)