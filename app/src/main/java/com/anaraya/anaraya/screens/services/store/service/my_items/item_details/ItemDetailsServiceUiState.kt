package com.anaraya.anaraya.screens.services.store.service.my_items.item_details

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.address.add_address.AllCompaniesUiData
import com.anaraya.anaraya.screens.address.add_address.CompanyAddressUiItem
import com.anaraya.anaraya.screens.services.store.service.my_items.ProductStoreItemServiceState
import kotlinx.coroutines.flow.Flow


data class ItemDetailsServiceUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val isSucceedConfirmDeal: Boolean = false,
    val confirmDealMsg: String? = "",
    val isSucceedProceedWithSale: Boolean = false,
    val product: ProductStoreItemServiceState? = null,
    val isSucceedRequestToDelete: Boolean = false,
    val visibilityRejectedRequest: Boolean = false,
    val visibilityProceedWithSale: Boolean = false,
    val visibilityProceedWithSaleDone: Boolean = false,
    val visibilityProceedWithSaleDoneAndRayaIsHandleDelivery: Boolean = false,
    val visibilityCustomerInfo: Boolean = false,
    val visibilityConfirmDealText: Boolean = false,
    val visibilityConfirmDealPayment: Boolean = false,
    val visibilityDeliverySchedule: Boolean = false,
    val visibilityBankAmount: Boolean = false,
    val visibilityCardCash: Boolean = false,
    val visibilityButtons: Boolean = false,
    val isSucceedRequestToBuy: Boolean = false,
    val requestToBuyMessage: String? = null,
    val error: String? = null,
    val requestToDeleteMsg: String? = null,
    val sellingStatus: Int = 0,
    val selectedMethod: String? = null,
    val iban: String = "",
    val companyName: String = "",
    val branchName: String = "",
    val cashStatus: Int = -1,
    val allCompanies: List<AllCompaniesUiData> = emptyList(),
    val allGovernorate: List<String> = emptyList(),
    val allCompanyAddresses: Flow<PagingData<CompanyAddressUiItem>>? = null,
    val companyError: Boolean = false,
    val governorateCompanyError: Boolean = false,
    val companyAddressError: Boolean = false,
    val chooseCustomerError: Boolean = false,
    val listeningIds: List<Int> = emptyList()
)


/* to Compare date
val date1 = "2024-07-30"
    val date2 = "2024-07-30"

    // Parse the dates
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val localDate1 = LocalDate.parse(date1, formatter)
    val localDate2 = LocalDate.parse(date2, formatter)

    // Compare the dates
    val comparisonResult = localDate1.compareTo(localDate2)

    when {
        comparisonResult < 0 -> println("Date1 is before Date2")
        comparisonResult > 0 -> println("Date1 is after Date2")
        else -> println("Dates are equal")
    }
 */