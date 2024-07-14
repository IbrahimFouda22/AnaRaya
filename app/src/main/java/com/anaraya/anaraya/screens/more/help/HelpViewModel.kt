package com.anaraya.anaraya.screens.more.help

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.Help
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageHelpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val manageHelpUseCase: ManageHelpUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {
    private val _helpUiState = MutableStateFlow(HelpUiState())
    val helpUiState = _helpUiState.asStateFlow()

    init {
        getAllHelp()
    }

    fun getAllData(){
        getAllHelp()
    }
    private fun getAllHelp(){
        _helpUiState.update {
            HelpUiState(
                isLoading = true,error = null, helpUiStateList = emptyList()
            )
        }

        try {
            viewModelScope.launch {
                onGetHelpSuccess(manageHelpUseCase.getAllHelp())
            }
        }catch (e:NoInternetException){
            onGetHelpFailure(context.getString(R.string.no_internet))
        }catch (e:Exception){
            onGetHelpFailure(e.message)
        }
    }

    private fun onGetHelpSuccess(allHelp: Help) {
        _helpUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                helpUiStateList = allHelp.data.map {data->
                    HelpUiStateData(
                        data.id,data.topic
                    )
                }
            )
        }
    }

    private fun onGetHelpFailure(response: String?) {
        _helpUiState.update {
            it.copy(
                isLoading = false,
                error = response,
                helpUiStateList = emptyList()
            )
        }
    }

    /*private fun searchHelp(){
        _helpUiState.update {
            HelpUiState(
                isLoading = true,error = null, helpUiStateList = emptyList()
            )
        }

        try {
            viewModelScope.launch {
                onSearchHelpSuccess(manageHelpUseCase.getAllHelp())
            }
        }catch (e:NoInternetException){
            onSearchHelpFailure(context.getString(R.string.no_internet))
        }catch (e:Exception){
            onSearchHelpFailure(e.message)
        }
    }

    private fun onSearchHelpSuccess(allHelp: Help) {
        _helpUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                helpUiStateList = allHelp.data.map {data->
                    HelpUiStateData(
                        data.id,data.topic
                    )
                }
            )
        }
    }

    private fun onSearchHelpFailure(response: String?) {
        _helpUiState.update {
            it.copy(
                isLoading = false,
                error = response,
                helpUiStateList = emptyList()
            )
        }
    }*/

    fun update(list: List<HelpUiStateData>){
        _helpUiState.update {
            HelpUiState(
               error = null, helpUiStateList = list
            )
        }
    }

}