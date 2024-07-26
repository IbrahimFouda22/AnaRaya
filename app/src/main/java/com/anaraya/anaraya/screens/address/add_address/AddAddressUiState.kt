package com.anaraya.anaraya.screens.address.add_address

import androidx.paging.PagingData
import com.anaraya.domain.entity.CompanyAddressItem
import kotlinx.coroutines.flow.Flow

data class AddAddressUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSucceed: Boolean = false,
    val apartment: Boolean = true,
    val office: Boolean = false,
    val addAddressUiState: String? = null,
    val allCompanies: List<AllCompaniesUiData> = emptyList(),
    val allGovernorate: List<String> = emptyList(),
    val allCompanyAddresses: Flow<PagingData<CompanyAddressUiItem>>? = null,
    val companyError: Boolean = false,
    val governorateCompanyError: Boolean = false,
    val companyAddressError: Boolean = false,
    val addressLabelError: Boolean = false,
    val governorateUserError: Boolean = false,
    val districtError: Boolean = false,
    val addressError: Boolean = false,
    val streetError: Boolean = false,
    val buildingError: Boolean = false,
    val landMarkError: Boolean = false,
)

data class AllCompaniesUiData(
    val id: Int,
    val name: String
)

data class CompanyAddressUiItem(
    val id: String,
    val office: String,
    val governorate: String,
    val companyName: String,
    val dayOfDelivery: Int,
    val deliveryFrom: String,
    val deliveryTo: String,
    val addedToFavouriteOrNot: Boolean,
){
    var isSelected = false
}

fun CompanyAddressItem.toUiState(): CompanyAddressUiItem {
    return CompanyAddressUiItem(
        id,
        office,
        governorate,
        companyName,
        dayOfDelivery,
        deliveryFrom,
        deliveryTo,
        addedToFavouriteOrNot
    )
}