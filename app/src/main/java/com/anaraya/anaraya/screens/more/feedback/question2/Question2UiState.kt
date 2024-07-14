package com.anaraya.anaraya.screens.more.feedback.question2

data class Question2UiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val isSucceedAddFeedBack: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val messageAddFeedBack: String? = null,
    val question: String? = null,
)
