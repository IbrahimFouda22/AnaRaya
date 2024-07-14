package com.anaraya.anaraya.screens.services

data class ServicesUiState(
    val isLoading:Boolean = false,
    val error:String?=null,
    val uploadMsg:String?=null,
    val navigateToItems :Boolean = false,
    val navigateToService:Boolean = false,
)