package com.anaraya.anaraya.screens.services.store.product.my_items.item_details

import androidx.paging.PagingData
import com.anaraya.anaraya.screens.address.add_address.AllCompaniesUiData
import com.anaraya.anaraya.screens.address.add_address.CompanyAddressUiItem
import com.anaraya.anaraya.screens.services.store.product.my_items.ProductStoreItemState
import kotlinx.coroutines.flow.Flow


data class ItemDetailsProductUiState(
    val isLoading: Boolean = false,
    val isSucceed: Boolean = false,
    val isSucceedConfirmDeal: Boolean = false,
    val isSucceedProceedWithSale: Boolean = false,
    val product: ProductStoreItemState? = null,
    val isSucceedRequestToDelete: Boolean = false,
    val visibilityProceedWithSale: Boolean = false,
    val visibilityProceedWithSaleDone: Boolean = false,
    val visibilityProceedWithSaleDoneAndRayaIsHandleDelivery: Boolean = false,
    val visibilityCustomerInfo: Boolean = false,
    val visibilityConfirmDeal: Boolean = false,
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
)
