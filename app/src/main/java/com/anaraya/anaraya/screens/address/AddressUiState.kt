package com.anaraya.anaraya.screens.address

import com.anaraya.anaraya.screens.shared_data.AddressUiState
import com.anaraya.domain.entity.CompanyAddressItem

data class AddressUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSucceed: Boolean = false,
    val message: String? = null,
    val delMessage: String? = null,
    val errorChangeDefaultAddress: String? = null,
    val changeAddressUiState: ChangeAddressUiState? = null,
    val addressUiState: List<AddressUiStateData> = emptyList(),
    val companyAddressUiState: List<AddressUiStateData> = emptyList(),
    val allAddressesUiState: List<AddressUiStateData> = emptyList(),
    val navigateToAddAddress: Boolean = false,
    val navigateToEditAddress: Boolean = false,
)

data class AddressUiStateData(
    val addressUiState: AddressUiState,
    val defaultAddress: Boolean,
    val isUserAddress: Boolean
){
    var checked = defaultAddress
}

data class ChangeAddressUiState(
    val message: String,
    val statusCode: Int
)

/*fun AddressUiStateData.toUiState(): AddressUiState {
    return AddressUiState(
        id, addressLabel, governorate, district, address, street, building, landmark
    )
}*/

fun CompanyAddressItem.toAddressUiState(): AddressUiStateData {
    return AddressUiStateData(
        defaultAddress = defaultAddress,
        isUserAddress = false,
        addressUiState = AddressUiState(
            id = id,
            addressLabel = companyName,
            address = companyName,
            governorate = governorate,
            district = "",
            landmark = "",
            building = "",
            street = "",
        ),
    )
}
