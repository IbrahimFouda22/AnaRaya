package com.anaraya.data.dto

data class AddressesDto(
    val data: AddressesDtoData,
    val isSucceed: Boolean,
    val message: String?
)

data class AddressesDtoData(
    val userAddresses:List<AddressDtoData>,
    val favouriteCompanyAddresses:List<CompanyAddressItem>,
)

