package com.anaraya.anaraya.screens.services.store.service.my_items.item_details

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.address.add_address.AllCompaniesUiData
import com.anaraya.anaraya.screens.address.add_address.toUiState
import com.anaraya.anaraya.screens.services.store.service.my_items.toState
import com.anaraya.domain.entity.BankAccount
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Company
import com.anaraya.domain.entity.CompanyAddressItem
import com.anaraya.domain.entity.CompanyGovernorate
import com.anaraya.domain.entity.ServiceStoreItemList
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageAddressesUseCase
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
@RequiresApi(Build.VERSION_CODES.O)
class ItemDetailsServiceViewModel @AssistedInject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    private val manageAddressesUseCase: ManageAddressesUseCase,
    @ApplicationContext private val context: Context,
    @Assisted private val serviceId: Int,
) : ViewModel() {
    private val _product = MutableStateFlow(ItemDetailsServiceUiState())
    val product = _product as StateFlow<ItemDetailsServiceUiState>

    init {
        getStoreProductByIdForOwner()
    }

    fun getStoreProductByIdForOwner() {
        _product.update {
            ItemDetailsServiceUiState(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetProductSuccess(manageStoreUseCase.getStoreServiceByIdForOwner(serviceId = serviceId))
            } catch (e: NoInternetException) {
                onGetProductFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetProductFailure(e.message.toString())
            }
        }
    }

    //    response.customerInformation.isNotEmpty() && response.customerWantsToRent.isNotEmpty() && response.userAction != 4
//    visibilityProceedWithSaleDone = response.customerInformation.isNotEmpty() && response.customerWantsToRent.isNotEmpty() && response.userAction == 4,
//                visibilityProceedWithSaleDoneAndRayaIsHandleDelivery = response.customerInformation.isEmpty() && response.userAction == 4,
//    visibilityButtons = response.userAction != 4 && response.userAction != -1 ,
    private fun onGetProductSuccess(response: ServiceStoreItemList) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                product = response.toState(visibilityBadge = false, isListed = false),
                visibilityRejectedRequest = response.status == 3,
                listeningIds = response.customerInformation.mapNotNull { information ->
                    if (information.rentStatus == 1)
                        information.listiningId
                    else
                        null
                },
                visibilityProceedWithSale = response.customerInformation.any { customerInformation ->
                    customerInformation.rentStatus == 1
                },
                visibilityCustomerInfo = response.customerInformation.any { customerInformation ->
                    if (customerInformation.rentStatus == 3) {
                        getAllCompanies()
                        getBankAccount()
                    }
                    customerInformation.rentStatus == 3
                },
                visibilityButtons = response.customerInformation.isEmpty() || response.customerInformation.all { customerInformation ->
                    customerInformation.rentStatus == 1 || customerInformation.rentStatus == 5
                },
            )
        }
        _product.update {
            it.copy(
                visibilityProceedWithSaleDone = it.visibilityCustomerInfo && !it.visibilityProceedWithSale
            )
        }
    }

    private fun onGetProductFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                product = null
            )
        }
    }

    fun requestToDelete(customerProductId: Int) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                requestToDeleteMsg = null
            )
        }
        viewModelScope.launch {
            try {
                onRequestToDeleteSuccess(manageStoreUseCase.requestToDeleteService(customerProductId))
            } catch (e: NoInternetException) {
                onRequestToDeleteFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onRequestToDeleteFailure(e.message.toString())
            }
        }
    }

    private fun onRequestToDeleteSuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                requestToDeleteMsg = response.message,
                isSucceedRequestToDelete = response.isSucceed
            )
        }
    }

    private fun onRequestToDeleteFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                requestToDeleteMsg = null,
                isSucceedRequestToDelete = false
            )
        }
    }

    private fun getBankAccount() {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onGetBankAccountSuccess(manageStoreUseCase.getBankAccount())
            } catch (e: NoInternetException) {
                onGetBankAccountFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetBankAccountFailure(e.message.toString())
            }
        }
    }

    private fun onGetBankAccountSuccess(response: BankAccount) {
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                iban = response.data.iban,
                companyName = response.data.holdingName,
                branchName = response.data.branch,
            )
        }
    }

    private fun onGetBankAccountFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                requestToDeleteMsg = null,
                isSucceedRequestToDelete = false
            )
        }
    }

    fun orderComplete(selectedCustomer: Int?) {
        if (selectedCustomer == null) {
            _product.update {
                it.copy(
                    chooseCustomerError = true
                )
            }
        } else {
            _product.update {
                it.copy(
                    chooseCustomerError = false,
                    visibilityCustomerInfo = false,
                    visibilityProceedWithSaleDone = false,
                    visibilityConfirmDealPayment = true,
                )
            }
            _product.update {
                it.copy(
                    visibilityConfirmDealText = !it.visibilityProceedWithSale
                )
            }
        }
    }

    fun proceedWithRent(listeningId: Int) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                isSucceedProceedWithSale = false
            )
        }
        viewModelScope.launch {
            try {
                onProceedWithSaleSuccess(manageStoreUseCase.proceedWithRent(listeningId))
            } catch (e: NoInternetException) {
                onProceedWithSaleFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onProceedWithSaleFailure(e.message.toString())
            }
        }
    }

    private fun onProceedWithSaleSuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                isLoading = true,
                isSucceedProceedWithSale = response.isSucceed
            )
        }
    }

    private fun onProceedWithSaleFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceedProceedWithSale = false
            )
        }
    }

    private fun getAllCompanies() {
//        showLoading(true)
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                allCompanyAddresses = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAllCompaniesSuccess(manageAddressesUseCase.getAllCompanies())
            } catch (e: NoInternetException) {
                onGetAllCompaniesFailure(context.getString(R.string.no_internet))
            } catch (e: Resources.NotFoundException) {
                onGetAllCompaniesFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllCompaniesFailure(e.message.toString())
            }
        }
    }

    private fun onGetAllCompaniesSuccess(response: Company) {
        //Log.d("companyAddress", "$response")
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                allCompanies = response.data.map { companyData ->
                    AllCompaniesUiData(companyData.id, companyData.name)
                }
            )
        }
    }

    private fun onGetAllCompaniesFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
            )
        }
    }


    fun getAllGovernorateByCompanyId(companyId: Int) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                allCompanyAddresses = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAllGovernorateByCompanyIdSuccess(
                    manageAddressesUseCase.getAllGovernorateByCompanyId(
                        companyId
                    )
                )
            } catch (e: NoInternetException) {
                onGetAllGovernorateByCompanyIdFailure(context.getString(R.string.no_internet))
            } catch (e: Resources.NotFoundException) {
                onGetAllGovernorateByCompanyIdFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllGovernorateByCompanyIdFailure(e.message.toString())
            }
        }
    }


    private fun onGetAllGovernorateByCompanyIdSuccess(response: CompanyGovernorate) {
        //Log.d("companyAddress", "$response")
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                allGovernorate = response.data.map { governorate ->
                    governorate
                }
            )
        }
    }


    private fun onGetAllGovernorateByCompanyIdFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
                allGovernorate = emptyList()
            )
        }
    }


    fun getAllCompanyAddresses(companyId: Int, governorate: String) {
        _product.update {
            it.copy(
                isLoading = true,
                error = null,
                allCompanyAddresses = null
            )
        }
        viewModelScope.launch {
            try {
                onGetAllCompanyAddressesSuccess(
                    manageAddressesUseCase.getAllCompanyAddresses(
                        companyId,
                        governorate
                    ).cachedIn(viewModelScope)
                )
            } catch (e: NoInternetException) {
                onGetAllCompanyAddressesFailure(context.getString(R.string.no_internet))
            } catch (e: Resources.NotFoundException) {
                onGetAllCompanyAddressesFailure(context.getString(R.string.no_titles))
            } catch (e: Exception) {
                onGetAllCompanyAddressesFailure(e.message.toString())
            }
        }
    }


    private fun onGetAllCompanyAddressesSuccess(response: Flow<PagingData<CompanyAddressItem>>) {
        //Log.d("companyAddress", "$response")
        _product.update {
            it.copy(
                isLoading = false,
                error = null,
                allCompanyAddresses = response.map { companyAddress ->
                    companyAddress.map { data ->
                        data.toUiState()
                    }
                }
            )
        }
    }


    private fun onGetAllCompanyAddressesFailure(error: String) {
        _product.update {
            it.copy(
                isLoading = false,
                error = error,
            )
        }
    }

    fun confirmDeal(
        company: String?,
        governorate: String?,
        companyAddress: String?,
        listeningId: Int,
        paymentMethod: Int,
    ) {

        _product.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onConfirmDealSuccess(
                    manageStoreUseCase.confirmServiceDeal(
                        company,
                        governorate,
                        listeningId,
                        companyAddress,
                        paymentMethod
                    )
                )
            } catch (e: EmptyDataException) {
                onConfirmDealFailure(e.message.toString())
            } catch (e: NoInternetException) {
                onConfirmDealFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onConfirmDealFailure(e.message.toString())
            }
        }
    }


    private fun onConfirmDealSuccess(response: BaseResponse) {
        _product.update {
            it.copy(
                error = null,
                isSucceedConfirmDeal = response.isSucceed,
                confirmDealMsg = response.message
            )
        }
    }


    private fun onConfirmDealFailure(error: String) {
        when (error) {
            context.getString(R.string.company_is_empty) -> {
                _product.update {
                    it.copy(
                        isLoading = false,
                        companyError = true
                    )
                }
            }

            context.getString(R.string.governorate_company_is_empty) -> {
                _product.update {
                    it.copy(
                        isLoading = false,
                        companyError = false,
                        governorateCompanyError = true
                    )
                }
            }

            context.getString(R.string.company_address_is_empty) -> {
                _product.update {
                    it.copy(
                        isLoading = false,
                        governorateCompanyError = false,
                        companyAddressError = true
                    )
                }
            }

            else -> {
                _product.update {
                    it.copy(
                        isLoading = false,
                        governorateCompanyError = false,
                        companyAddressError = false,
                        error = error,
                    )
                }
            }
        }
    }

    fun resetMsg() {
        _product.update {
            it.copy(
                isLoading = false,
                requestToDeleteMsg = null,
                confirmDealMsg = null
            )
        }
    }

    fun setSelectedMethod(method: String) {
        _product.update {
            it.copy(
                selectedMethod = method,
                visibilityDeliverySchedule = it.cashStatus == 1 || it.cashStatus == 2,
                visibilityBankAmount = it.cashStatus == 3,
                visibilityCardCash = true
            )
        }
    }

    fun setCashNum(cashStatus: Int) {
        _product.update {
            it.copy(
                cashStatus = cashStatus,
            )
        }
    }

    @AssistedFactory
    interface ItemDetailsServiceAssistedFactory {
        fun create(serviceId: Int): ItemDetailsServiceViewModel
    }

    companion object {
        fun createItemDetailsProductFactory(
            assistedFactory: ItemDetailsServiceAssistedFactory,
            serviceId: Int,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(serviceId) as T
                }
            }
        }
    }
}