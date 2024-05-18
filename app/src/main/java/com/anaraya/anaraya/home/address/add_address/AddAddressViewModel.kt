package com.anaraya.anaraya.home.address.add_address

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.AddUpdateAddress
import com.anaraya.domain.entity.Company
import com.anaraya.domain.entity.CompanyAddressItem
import com.anaraya.domain.entity.CompanyGovernorate
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val manageAddressesUseCase: ManageAddressesUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _addAddressUiState = MutableStateFlow(AddAddressUiState())
    val addAddressUiState = _addAddressUiState as StateFlow<AddAddressUiState>

    private val _addressTypeState = MutableStateFlow(AddAddressUiState())
    val addressTypeState = _addressTypeState as StateFlow<AddAddressUiState>


    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState as StateFlow<Boolean>

    init {
        getAllCompanies()
    }

    fun addUserAddress(
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ) {

        showLoading(true)
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onAddUserAddressSuccess(
                    manageAddressesUseCase.addAddress(
                        addressLabel,
                        governorate,
                        district,
                        address,
                        street,
                        building,
                        landmark
                    )
                )
            } catch (e: NoInternetException) {
                onAddUserAddressFailure("No Internet")
            }catch (e: Exception) {
                onAddUserAddressFailure(e.message.toString())
            }
        }
    }


    private fun onAddUserAddressSuccess(response: AddUpdateAddress) {
        showLoading(false)
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = null,
                isSucceed = response.isSucceed,
                addAddressUiState = response.message
            )
        }
    }


    private fun onAddUserAddressFailure(error: String) {
        showLoading(false)
        when (error) {
            context.getString(R.string.address_label_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        addressLabelError = true
                    )
                }
            }
            context.getString(R.string.governorate_user_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        governorateUserError = true
                    )
                }
            }
            context.getString(R.string.district_label_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        districtError = true
                    )
                }
            }
            context.getString(R.string.address_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        addressError = true
                    )
                }
            }
            context.getString(R.string.street_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        streetError = true
                    )
                }
            }
            context.getString(R.string.building_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        buildingError = true
                    )
                }
            }
            context.getString(R.string.landmark_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        landMarkError = true
                    )
                }
            }
            else -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        error = error,
                    )
                }
            }
        }

    }

    fun addCompanyAddress(
        company: String?,
        governorate: String?,
        companyAddress: String?,
    ) {

        showLoading(true)
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onAddCompanyAddressSuccess(
                    manageAddressesUseCase.addCompanyAddress(
                        company,
                        governorate,
                        companyAddress,
                    )
                )
            }catch (e: EmptyDataException) {
                onAddCompanyAddressFailure(e.message.toString())
            }  catch (e: NoInternetException) {
                onAddCompanyAddressFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddCompanyAddressFailure(e.message.toString())
            }
        }
    }


    private fun onAddCompanyAddressSuccess(response: AddUpdateAddress) {
        showLoading(false)
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = null,
                isSucceed = response.isSucceed,
                addAddressUiState = response.message
            )
        }
    }


    private fun onAddCompanyAddressFailure(error: String) {
        showLoading(false)
        when (error) {
            context.getString(R.string.company_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        companyError = true
                    )
                }
            }

            context.getString(R.string.governorate_company_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        governorateCompanyError = true
                    )
                }
            }

            context.getString(R.string.company_address_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        companyAddressError = true
                    )
                }
            }
            else -> {
                _addAddressUiState.update {
                    AddAddressUiState(
                        isLoading = false,
                        error = error,
                    )
                }
            }
        }
    }

    fun setApartment() {
        _addressTypeState.update {
            it.copy(
                apartment = true, office = false
            )
        }
    }

    fun setOffice() {
        //showLoading(true)
        _addressTypeState.update {
            it.copy(
                apartment = false, office = true
            )
        }
        //getAllCompanyAddresses()
    }

    private fun getAllCompanies() {
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetAllCompaniesSuccess(manageAddressesUseCase.getAllCompanies())
            } catch (e: NoInternetException) {
                onGetAllCompaniesFailure(context.getString(R.string.no_internet))
            } catch (e: NotFoundException) {
                onGetAllCompaniesFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllCompaniesFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllCompaniesSuccess(response: Company) {
        showLoading(false)
        //Log.d("companyAddress", "$response")
        _addAddressUiState.update {
            AddAddressUiState(
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
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = error,
            )
        }
    }


    fun getAllGovernorateByCompanyId(companyId: Int) {
        showLoading(true)
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetAllGovernorateByCompanyIdSuccess(manageAddressesUseCase.getAllGovernorateByCompanyId(companyId))
            } catch (e: NoInternetException) {
                onGetAllGovernorateByCompanyIdFailure(context.getString(R.string.no_internet))
            } catch (e: NotFoundException) {
                onGetAllGovernorateByCompanyIdFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllGovernorateByCompanyIdFailure(e.message.toString())
            }
        }
    }


    private fun onGetAllGovernorateByCompanyIdSuccess(response: CompanyGovernorate) {
        showLoading(false)
        //Log.d("companyAddress", "$response")
        _addAddressUiState.update {
            AddAddressUiState(
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
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = error,
                allGovernorate = emptyList()
            )
        }
    }


    fun getAllCompanyAddresses(companyId: Int,governorate: String) {
        showLoading(true)
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetAllCompanyAddressesSuccess(manageAddressesUseCase.getAllCompanyAddresses(companyId,governorate).cachedIn(viewModelScope))
            } catch (e: NoInternetException) {
                onGetAllCompanyAddressesFailure(context.getString(R.string.no_internet))
            } catch (e: NotFoundException) {
                onGetAllCompanyAddressesFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllCompanyAddressesFailure(e.message.toString())
            }
        }
    }


    private fun onGetAllCompanyAddressesSuccess(response: Flow<PagingData<CompanyAddressItem>>) {
        showLoading(false)
        //Log.d("companyAddress", "$response")
        _addAddressUiState.update {
            AddAddressUiState(
                isLoading = false,
                error = null,
                allCompanyAddresses = response.map {
                    it.map {data->
                        data.toUiState()
                    }
                }
            )
        }
    }


    private fun onGetAllCompanyAddressesFailure(error: String) {
        showLoading(false)
        _addAddressUiState.update {
            AddAddressUiState(
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

}