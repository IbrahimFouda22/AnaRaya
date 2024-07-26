package com.anaraya.anaraya.screens.services.store.service.my_items.edit_service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.home.CategoryUiState
import com.anaraya.anaraya.screens.services.store.service.my_items.toState
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Category
import com.anaraya.domain.entity.ServiceStoreItemList
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

@Suppress("UNCHECKED_CAST")
@RequiresApi(Build.VERSION_CODES.O)

class EditServiceViewModel @AssistedInject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @ApplicationContext private val context: Context,
    @Assisted private val serviceId: Int,
) :
    ViewModel() {

    private val _editItemServiceUiState = MutableStateFlow(EditServiceUiState())
    val editItemServiceUiState = _editItemServiceUiState as StateFlow<EditServiceUiState>

    fun getAllData() {
        getStoreServiceByIdForOwner()
    }

    private fun getStoreServiceByIdForOwner() {
        _editItemServiceUiState.update {
            it.copy(
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

    private fun onGetProductSuccess(response: ServiceStoreItemList) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                product = response.toState(
                    visibilityBadge = false,
                    isListed = false,
                    isEditing = true
                )
            )
        }
        getCategories()
    }

    private fun onGetProductFailure(error: String) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                sellCategoriesList = emptyList()
            )
        }
    }

    private fun getCategories() {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetCategoriesSuccess(manageStoreUseCase.getStoreProductCategories())
            } catch (e: NoInternetException) {
                onGetCategoriesFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetCategoriesFailure(e.message.toString())
            }
        }
    }

    private fun onGetCategoriesSuccess(response: List<Category>) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                sellCategoriesList = response.map { data ->
                    CategoryUiState(
                        id = data.id,
                        name = data.name,
                        image = null
                    )
                }
            )
        }
    }

    private fun onGetCategoriesFailure(error: String) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                sellCategoriesList = emptyList()
            )
        }
    }

    fun getSubCategories(categoryId: Int) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                sellSubCategoriesList = emptyList()
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onGetSubCategoriesSuccess(manageStoreUseCase.getStoreSubCategory(categoryId))
            } catch (e: NoInternetException) {
                onGetSubCategoriesFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetSubCategoriesFailure(e.message.toString())
            }
        }
    }

    private fun onGetSubCategoriesSuccess(response: List<Category>) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                sellSubCategoriesList = response.map { data ->
                    CategoryUiState(
                        id = data.id,
                        name = data.name,
                        image = null
                    )
                }
            )
        }
    }

    private fun onGetSubCategoriesFailure(error: String) {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                sellSubCategoriesList = emptyList()
            )
        }
    }


    fun editItem(
        id: Int,
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        productImage: File?,
        isServiceRental: Boolean?,
        fromDate: String,
        toDate: String,
        accept: Boolean,
        imageUrl: String?,
    ) {
        resetErrors()
        _editItemServiceUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onAddItemSuccess(
                    manageStoreUseCase.storeUpdateService(
                        id,
                        title,
                        categoryId,
                        subCategoryId,
                        itemDescription,
                        price,
                        location,
                        productImage,
                        accept,
                        isServiceRental,
                        fromDate,
                        toDate,
                        imageUrl
                    )
                )
            } catch (e: NoInternetException) {
                onAddUserAddressFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddUserAddressFailure(e.message.toString())
            }
        }
    }


    private fun onAddItemSuccess(response: BaseResponse?) {
        response?.let {
            _editItemServiceUiState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    isSucceed = response.isSucceed,
                    addProductMsg = response.message,
                    isSucceedEditProduct = response.isSucceed
                )
            }
        }
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                error = null,
            )
        }
    }


    private fun onAddUserAddressFailure(error: String) {
        when (error) {
            context.getString(R.string.title_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        titleError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.category_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        categoryError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.sub_category_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        subCategoryError = true,
                        focusError = true
                    )
                }
            }


            context.getString(R.string.description_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        descriptionError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.price_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        priceError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.location_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        locationError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.image_field_is_empty) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        pictureError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.must_accept_terms_and_conditions) -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        conditionAndTermsError = true,
                        focusError = true
                    )
                }
            }

            else -> {
                _editItemServiceUiState.update {
                    it.copy(
                        isLoading = false,
                        error = error,
                        focusError = false,
                        isSucceedEditProduct = false
                    )
                }
            }
        }

    }

    private fun resetErrors() {
        _editItemServiceUiState.update {
            it.copy(
                isLoading = false,
                titleError = false,
                categoryError = false,
                subCategoryError = false,
                descriptionError = false,
                priceError = false,
                locationError = false,
                pictureError = false,
                conditionAndTermsError = false,
            )
        }
    }

    fun updateStateTermsAndCondition() {
        _editItemServiceUiState.update {
            it.copy(
                conditionAndTermsError = false,
            )
        }
    }

    fun resetMsg() {
        _editItemServiceUiState.update {
            it.copy(
                addProductMsg = null
            )
        }
    }

    fun visibilityServiceRental(boolean: Boolean) {
        _editItemServiceUiState.update {
            it.copy(
                isServiceRental = boolean
            )
        }
    }

    @AssistedFactory
    interface EditServiceAssistedFactory {
        fun create(id: Int): EditServiceViewModel
    }

    companion object {
        fun createEditServiceFactory(
            assistedFactory: EditServiceAssistedFactory,
            id: Int,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(id) as T
                }
            }
        }
    }
}