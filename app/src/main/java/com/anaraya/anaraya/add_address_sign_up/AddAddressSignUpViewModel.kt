package com.anaraya.anaraya.add_address_sign_up

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.AddressUiStateData
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AddAddressSignUpViewModel @AssistedInject constructor(
    private val manageAddressesUseCase: ManageAddressesUseCase,
    @Assisted private val data: AddressUiStateData?,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _addAddressUiState = MutableStateFlow(AddAddressSignUpUiState())
    val addAddressUiState = _addAddressUiState as StateFlow<AddAddressSignUpUiState>
    val addressLabel = MutableStateFlow("")
    val governorate = MutableStateFlow("")
    val district = MutableStateFlow("")
    val address = MutableStateFlow("")
    val street = MutableStateFlow("")
    val building = MutableStateFlow("")
    val landmark = MutableStateFlow("")

    init {
        data?.let {
            addressLabel.value = data.addressLabel
            governorate.value = data.governorate
            district.value = data.district
            address.value = data.address
            street.value = data.street
            building.value = data.building
            landmark.value = data.landmark
        }
    }

    fun addUserAddress() {
        _addAddressUiState.update {
            AddAddressSignUpUiState(
                isLoading = false,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                if (addressLabel.value.isEmpty() && governorate.value.isEmpty() && district.value.isEmpty() && address.value.isEmpty() &&
                    street.value.isEmpty() && building.value.isEmpty() && landmark.value.isEmpty()
                ){
                    _addAddressUiState.update {
                        AddAddressSignUpUiState(
                            isLoading = false,
                            error = null,
                            isSucceed = true,
                            addressUiStateData = null
                        )
                    }
                }
                else{
                    manageAddressesUseCase.checkDataValidation(
                        addressLabel.value,
                        governorate.value,
                        district.value,
                        address.value,
                        street.value,
                        building.value,
                        landmark.value
                    )
                    _addAddressUiState.update {
                        AddAddressSignUpUiState(
                            isLoading = false,
                            error = null,
                            isSucceed = true,
                            addressUiStateData = AddressUiStateData(
                                addressLabel.value,
                                governorate.value,
                                district.value,
                                address.value,
                                street.value,
                                building.value,
                                landmark.value
                            )
                        )
                    }
                }
            } catch (e: NoInternetException) {
                onAddUserAddressFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddUserAddressFailure(e.message.toString())
            }
        }
    }


    private fun onAddUserAddressFailure(error: String) {
        when (error) {
            context.getString(R.string.address_label_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        addressLabelError = true
                    )
                }
            }

            context.getString(R.string.governorate_user_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        governorateUserError = true
                    )
                }
            }

            context.getString(R.string.district_label_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        districtError = true
                    )
                }
            }

            context.getString(R.string.address_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        addressError = true
                    )
                }
            }

            context.getString(R.string.street_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        streetError = true
                    )
                }
            }

            context.getString(R.string.building_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        buildingError = true
                    )
                }
            }

            context.getString(R.string.landmark_is_empty) -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        landMarkError = true
                    )
                }
            }

            else -> {
                _addAddressUiState.update {
                    AddAddressSignUpUiState(
                        isLoading = false,
                        error = error,
                    )
                }
            }
        }

    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(addAddressUiState: AddressUiStateData?): AddAddressSignUpViewModel
    }

    companion object {
        fun createProvider(
            addAddressUiState: AddressUiStateData?, factory: AssistedFactory
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(addAddressUiState) as T
                }
            }
        }
    }

}