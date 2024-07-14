package com.anaraya.anaraya.screens.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.address.add_address.AllCompaniesUiData
import com.anaraya.anaraya.screens.address.add_address.toUiState
import com.anaraya.domain.entity.Company
import com.anaraya.domain.entity.CompanyAddressItem
import com.anaraya.domain.entity.CompanyGovernorate
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel
@Inject constructor(
    private val manageAddressesUseCase: ManageAddressesUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {


    private val _scheduleUiState = MutableStateFlow(DeliveryScheduleUiState())
    val scheduleUiState = _scheduleUiState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    init {
        getAllCompanies()
    }

    fun getAllData() {
        getAllCompanies()
    }

    private fun getAllCompanies() {
        showLoading(true)
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetAllCompaniesSuccess(manageAddressesUseCase.getAllCompanies())
            } catch (e: NoInternetException) {
                onGetAllCompaniesFailure(context.getString(R.string.no_internet))
            } catch (e: Resources.NotFoundException) {
                onGetAllCompaniesFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllCompaniesFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllCompaniesSuccess(response: Company) {
        showLoading(false)
        //Log.d("companyAddress", "$response")
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = false,
                error = null,
                allCompanies = response.data.map {
                    AllCompaniesUiData(it.id, it.name)
                }
            )
        }
    }

    private fun onGetAllCompaniesFailure(error: String) {
        showLoading(false)
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = false,
                error = error,
            )
        }
    }


    fun getAllGovernorateByCompanyId(companyId: Int) {
        showLoading(true)
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetAllGovernorateByCompanyIdSuccess(
                    manageAddressesUseCase.getAllGovernorateByCompanyId(
                        companyId
                    )
                )
            } catch (e: NoInternetException) {
                onGetAllGovernorateByCompanyIdFailure(context.getString(R.string.no_internet))
            } catch (e: Resources.NotFoundException) {
                onGetAllGovernorateByCompanyIdFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllGovernorateByCompanyIdFailure(e.message.toString())
            }
        }
    }


    private fun onGetAllGovernorateByCompanyIdSuccess(response: CompanyGovernorate) {
        showLoading(false)
        //Log.d("companyAddress", "$response")
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = false,
                error = null,
                allGovernorate = response.data.map {
                    it
                }
            )
        }
    }


    private fun onGetAllGovernorateByCompanyIdFailure(error: String) {
        showLoading(false)
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = false,
                error = error,
                allGovernorate = emptyList()
            )
        }
    }


    fun getAllCompanyAddresses(companyId: Int, governorate: String) {
        showLoading(true)
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetAllCompanyAddressesSuccess(
                    manageAddressesUseCase.getAllCompanyAddresses(
                        companyId,
                        governorate
                    ).cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetAllCompanyAddressesFailure(context.getString(R.string.no_internet))
            } catch (e: Resources.NotFoundException) {
                onGetAllCompanyAddressesFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllCompanyAddressesFailure(e.message.toString())
            }
        }
    }


    private fun onGetAllCompanyAddressesSuccess(response: Flow<PagingData<CompanyAddressItem>>) {
        showLoading(false)
        //Log.d("companyAddress", "$response")
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = false,
                error = null,
                allCompanyAddresses = response.map {
                    it.map { data ->
                        data.toUiState()
                    }
                }
            )
        }
    }


    private fun onGetAllCompanyAddressesFailure(error: String) {
        showLoading(false)
        _scheduleUiState.update {
            DeliveryScheduleUiState(
                isLoading = false,
                error = error,
            )
        }
    }

    fun showLoading(boolean: Boolean) {
        _loadingState.update {
            boolean
        }
    }

    fun navigateToBack() {
        _scheduleUiState.update {
            it.copy(
                navigateToBack = true
            )
        }
    }

    fun navigateToBackDone() {
        _scheduleUiState.update {
            it.copy(
                navigateToBack = false
            )
        }
    }
}