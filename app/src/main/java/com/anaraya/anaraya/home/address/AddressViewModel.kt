package com.anaraya.anaraya.home.address

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.home.shared_data.toUiState
import com.anaraya.domain.entity.AddUpdateAddress
import com.anaraya.domain.entity.Addresses
import com.anaraya.domain.entity.ChangeDefaultAddress
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val manageAddressesUseCase: ManageAddressesUseCase

) :
    ViewModel() {

    private val _addressesUiState = MutableStateFlow(AddressUiState())
    val addressesUiState = _addressesUiState as StateFlow<AddressUiState>

    init {
        getAddresses()
    }

    fun getAllData() {
        getAddresses()
    }

    private fun getAddresses() {
        _addressesUiState.update {
            it.copy(isLoading = true, isSucceed = false, changeAddressUiState = null, errorChangeDefaultAddress = null)
        }
        viewModelScope.launch {
            try {
                onGetAddressesSuccess(manageAddressesUseCase.getAddresses())
            } catch (e: NoInternetException) {
                onGetAddressesFailure("No Internet")
            } catch (e: NotFoundException) {
                onGetAddressesFailure("No Titles")
            } catch (e: Exception) {
                onGetAddressesFailure(e.message.toString())
            }
        }
    }


    private fun onGetAddressesSuccess(addresses: Addresses) {
        _addressesUiState.update {
            AddressUiState(
                isLoading = false,
                error = null,
                isSucceed = true,
                message = addresses.message,
                addressUiState = addresses.data.map { data ->
                    AddressUiStateData(
                        defaultAddress = data.defaultAddress,
                        isUserAddress = true,
                        addressUiState = data.toUiState(),
                    )
                },
                companyAddressUiState = addresses.companyAddresses.map {
                    it.toAddressUiState()
                },
            )
        }
        _addressesUiState.update {
            it.copy(
                allAddressesUiState = it.addressUiState + it.companyAddressUiState
            )
        }
    }


    private fun onGetAddressesFailure(error: String) {
        _addressesUiState.update {
            AddressUiState(
                error = error,
            )
        }
    }


    fun changeDefaultAddress(addressId: String, isUserAddress: Boolean) {
        _addressesUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null,
            )
        }
        viewModelScope.launch {
            try {
                onChangeDefaultAddressSuccess(
                    manageAddressesUseCase.changeAddressDefault(
                        addressId,
                        isUserAddress
                    )
                )
            } catch (e: NoInternetException) {
                onChangeDefaultAddressFailure("No Internet")
            } catch (e: Exception) {
                onChangeDefaultAddressFailure(e.message.toString())
            }
        }
    }


    private fun onChangeDefaultAddressSuccess(response: ChangeDefaultAddress) {
        _addressesUiState.update {
            it.copy(
                isLoading = false, error = null,
                errorChangeDefaultAddress = null,
                isSucceed = response.isSucceed,
                changeAddressUiState = ChangeAddressUiState(
                    message = response.message,
                    statusCode = response.statusCode
                )
            )
        }
    }


    private fun onChangeDefaultAddressFailure(error: String) {
        _addressesUiState.update {
            it.copy(
                isLoading = false, error = error, changeAddressUiState = null,
                errorChangeDefaultAddress = error, isSucceed = false
            )
        }
    }

    fun deleteAddress(addressId: String) {
        _addressesUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                delMessage = null,
            )
        }
        viewModelScope.launch {
            try {
                onDeleteAddressSuccess(manageAddressesUseCase.deleteAddress(addressId))
            } catch (e: NoInternetException) {
                onDeleteAddressFailure("No Internet")
            } catch (e: Exception) {
                onDeleteAddressFailure(e.message.toString())
            }
        }
    }


    private fun onDeleteAddressSuccess(response: AddUpdateAddress) {
        _addressesUiState.update {
            it.copy(
                isLoading = false, error = null,
                delMessage = response.message,
            )
        }
        getAddresses()
    }


    private fun onDeleteAddressFailure(error: String) {
        _addressesUiState.update {
            it.copy(
                isLoading = false, error = error, delMessage = null,
            )
        }
    }


    fun navigateToAddAddress() {
        _addressesUiState.update {
            it.copy(
                navigateToAddAddress = true,
                error = null,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null
            )
        }
    }

    fun navigateToAddAddressDone() {
        _addressesUiState.update {
            it.copy(
                navigateToAddAddress = false,
                error = null,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null
            )
        }
    }

    fun navigateToEditAddress() {
        _addressesUiState.update {
            it.copy(
                navigateToEditAddress = true,
                error = null,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null
            )
        }
    }

    fun navigateToEditAddressDone() {
        _addressesUiState.update {
            it.copy(
                navigateToEditAddress = false,
                error = null,
                changeAddressUiState = null,
                errorChangeDefaultAddress = null
            )
        }
    }

}