package com.anaraya.anaraya.screens.more.promo

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.PromoCode
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManagePromoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoViewModel @Inject constructor(
    private val managePromoUseCase: ManagePromoUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {
    private val _promoUiState = MutableStateFlow(PromoCodeUiState())
    val promoUiState = _promoUiState.asStateFlow()

    init {
        getAllPromo()
    }

    fun getAllData(){
        getAllPromo()
    }
    private fun getAllPromo(){
        _promoUiState.update {
            PromoCodeUiState(
                isLoading = true
            )
        }

        try {
            viewModelScope.launch {
                onGetAllPromoSuccess(managePromoUseCase.getAllPromoCodes())
            }
        }catch (e:NoInternetException){
            onGetAllPromoFailure(context.getString(R.string.no_internet))
        }catch (e:Exception){
            onGetAllPromoFailure(e.message)
        }
    }

    private fun onGetAllPromoSuccess(response: PromoCode) {
        _promoUiState.update {
            PromoCodeUiState(
                isLoading = false,
                isSucceed = response.isSucceed,
                promoUiItem = response.data.map {
                    it.toUiItem()
                }
            )
        }
    }

    private fun onGetAllPromoFailure(response: String?) {
        _promoUiState.update {
            PromoCodeUiState(
                isLoading = false,
                error = response,
            )
        }
    }

}