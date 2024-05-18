package com.anaraya.anaraya.home.more.feedback

data class FeedBackUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val stateViewPager: Int = -1,
    val rate: Int = 0
)
