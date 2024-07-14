package com.anaraya.anaraya.screens.schedule

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.address.add_address.AllCompaniesUiData
import com.anaraya.anaraya.screens.address.add_address.CompanyAddressUiItem
import kotlinx.coroutines.flow.Flow

data class DeliveryScheduleUiState(
    val isLoading: Boolean = false,
    val navigateToBack: Boolean = false,
    val error: String? = null,
    val deliveryScheduleImage: String? = null,

    val allCompanies: List<AllCompaniesUiData> = emptyList(),
    val allGovernorate: List<String> = emptyList(),
    val allCompanyAddresses: Flow<PagingData<CompanyAddressUiItem>>? = null,
)
