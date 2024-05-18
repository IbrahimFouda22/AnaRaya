package com.anaraya.anaraya.home.address.interaction

import com.anaraya.anaraya.home.shared_data.AddressUiState

interface AddressInteraction {
    fun onClickChange(address: AddressUiState)
    fun onClickSwitch(addressId: String, isUserAddress: Boolean)
    fun onClickDelete(addressId: String)
}