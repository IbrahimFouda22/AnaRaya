package com.anaraya.anaraya.landing.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewPagerViewModel :ViewModel() {
    private val _navigateToLogin = MutableLiveData(false)
    val navigateToLogin :LiveData<Boolean> get() = _navigateToLogin
    private val _navigateToHomeDesign = MutableLiveData(false)
    val navigateToHomeDesign :LiveData<Boolean> get() = _navigateToHomeDesign

    fun navigateToLogin(){
        _navigateToLogin.value = true
    }
    fun navigateToLoginDone(){
        _navigateToLogin.value = false
    }
    fun navigateToHomeDesign(){
        _navigateToHomeDesign.value = true
    }
    fun navigateToHomeDesignDone(){
        _navigateToHomeDesign.value = false
    }
}