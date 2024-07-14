package com.anaraya.anaraya.screens.address.interaction

import com.anaraya.anaraya.screens.shared_data.AddressUiState

interface AddressInteraction {
    fun onClickChange(address: AddressUiState)
    fun onClickSwitch(addressId: String, isUserAddress: Boolean)
    fun onClickDelete(addressId: String)
}