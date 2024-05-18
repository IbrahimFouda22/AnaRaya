package com.anaraya.anaraya.landing.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.domain.exception.UnAuthException
import com.anaraya.domain.usecase.ManageAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Splash1ViewModel @Inject constructor(private val authUseCase: ManageAuthUseCase) : ViewModel() {

    private val _checkAuth = MutableStateFlow(SplashUiState())
    val checkState = _checkAuth as StateFlow<SplashUiState>

    /*fun checkAuth(rayaId:String,nationalId:String,password:String){
        viewModelScope.launch {
            _checkAuth.update {
                SplashUiState(
                    error = null
                )
            }
            try {
                onCheckAuthSuccess(authUseCase.checkAuth(rayaId, nationalId, password).isSucceed)
            }catch (e: UnAuthException){
                onCheckAuthFailure("unAuth")
            }catch (e:Exception){
                onCheckAuthFailure(e.message.toString())
            }

        }
    }

    private fun onCheckAuthSuccess(navigateToHome:Boolean) {
        _checkAuth.update {
            SplashUiState(
                error = null,
                navigateToHome = navigateToHome
            )
        }
    }
    private fun onCheckAuthFailure(error:String){
        _checkAuth.update {
            SplashUiState(
                error = error,
                navigateToHome = false,
            )
        }
    }*/
}