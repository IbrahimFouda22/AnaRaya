package com.anaraya.anaraya.screens.more

import com.anaraya.anaraya.screens.shared_data.ProfileUiState

data class MoreUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val uploadMsg: String? = null,
    val profileUiState: ProfileUiState? = null,
    val navigateToFeedBack: Boolean = false,
    val navigateToMyInfo: Boolean = false,
    val navigateToOrders: Boolean = false,
    val navigateToHelp: Boolean = false,
    val navigateToSchedule: Boolean = false,
    val navigateToAboutUs: Boolean = false,
    val navigateToFamily: Boolean = false,
    val navigateToTermsCondition: Boolean = false,
    val navigateToLanguage: Boolean = false,
    val navigateToPrivacyPolicy: Boolean = false,
    val navigateToSurveys: Boolean = false,
    val showSurveys:Boolean = false,
    val isFCMSent: Boolean = false,
    val loyaltyPoints: Int = 0,
)
