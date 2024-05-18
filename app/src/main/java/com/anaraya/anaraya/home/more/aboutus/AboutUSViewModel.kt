package com.anaraya.anaraya.home.more.aboutus

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.AboutUS
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAboutUsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutUSViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageAboutUsUseCase: ManageAboutUsUseCase
) : ViewModel() {
    private val _aboutUsUiState = MutableStateFlow(AboutUsUiState())
    val aboutUsUiState = _aboutUsUiState.asStateFlow()

    init {
        getAboutUs()
    }

    fun getAllData(){
        getAboutUs()
    }
    private fun getAboutUs() {
        _aboutUsUiState.update {
            AboutUsUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAboutUsSuccess(manageAboutUsUseCase.getAboutUs())
            } catch (e: NoInternetException) {
                onGetAboutUsFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetAboutUsFailure(e.message.toString())
            }
        }
    }

    private fun onGetAboutUsSuccess(response: AboutUS) {
        _aboutUsUiState.update {
            AboutUsUiState(
                isLoading = false,
                error = null,
                data = response.data,
            )
        }
    }

    private fun onGetAboutUsFailure(error: String) {
        _aboutUsUiState.update {
            AboutUsUiState(
                isLoading = false,
                error = error,
            )
        }
    }
}