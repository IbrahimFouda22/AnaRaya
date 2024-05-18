package com.anaraya.anaraya.home.address.edit_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.home.shared_data.AddressUiState
import com.anaraya.domain.entity.AddUpdateAddress
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAddressViewModel @AssistedInject constructor(
    private val manageAddressesUseCase: ManageAddressesUseCase,
    @Assisted private val addressUiState: AddressUiState
) :
    ViewModel() {

    private val _editAddressUiState = MutableStateFlow(EditAddressUiState())
    val editAddressUiState = _editAddressUiState.asStateFlow()

    val addressLabel = MutableStateFlow(addressUiState.addressLabel)
    val governorate = MutableStateFlow(addressUiState.governorate)
    val district = MutableStateFlow(addressUiState.district)
    val address = MutableStateFlow(addressUiState.address)
    val street = MutableStateFlow(addressUiState.street)
    val building = MutableStateFlow(addressUiState.building)
    val landmark = MutableStateFlow(addressUiState.landmark)

    fun getAllData(){
        editAddress()
    }

    fun editAddress() {

        _editAddressUiState.update {
            EditAddressUiState(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onEditAddressSuccess(
                    manageAddressesUseCase.updateAddress(
                        addressUiState.id,
                        addressLabel.value,
                        governorate.value,
                        district.value,
                        address.value,
                        street.value,
                        building.value,
                        landmark.value
                    )
                )
            } catch (e: NoInternetException) {
                onEditAddressFailure("No Internet")
            } catch (e: Exception) {
                onEditAddressFailure(e.message.toString())
            }
        }
    }


    private fun onEditAddressSuccess(response: AddUpdateAddress) {
        _editAddressUiState.update {
            EditAddressUiState(
                isLoading = false,
                error = null,
                editAddressUiState = response.message
            )
        }
    }


    private fun onEditAddressFailure(error: String) {
        _editAddressUiState.update {
            EditAddressUiState(
                isLoading = false,
                error = error,
                editAddressUiState = null
            )
        }
    }

    fun updateVisibility() {
        if (addressUiState.addressLabel == addressLabel.value && addressUiState.governorate == governorate.value &&
            addressUiState.district == district.value && addressUiState.address == address.value &&
            addressUiState.street == street.value && addressUiState.building == building.value &&
            addressUiState.landmark == landmark.value
        )
            _editAddressUiState.update {
                it.copy(saveIsVisible = false)
            }
        else
            _editAddressUiState.update {
                it.copy(saveIsVisible = true)
            }
    }

    @AssistedFactory
    interface EditAddressAssistedFactory {
        fun create(addressUiState: AddressUiState): EditAddressViewModel
    }

    companion object {
        fun createFactory(
            assistedFactory: EditAddressAssistedFactory,
            addressUiState: AddressUiState
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(addressUiState) as T
                }
            }
        }
    }

}