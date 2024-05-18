package com.anaraya.anaraya.home.more
import com.anaraya.anaraya.home.shared_data.ProfileUiState
data class MoreUiState(
    val isLoading:Boolean = false,
    val error:String?=null,
    val uploadMsg:String?=null,
    val profileUiState: ProfileUiState?=null,
    val navigateToFeedBack :Boolean = false,
    val navigateToMyInfo :Boolean = false,
    val navigateToOrders :Boolean = false,
    val navigateToHelp :Boolean = false,
    val navigateToSchedule :Boolean = false,
    val navigateToAboutUs :Boolean = false,
    val navigateToFamily :Boolean = false,
)
