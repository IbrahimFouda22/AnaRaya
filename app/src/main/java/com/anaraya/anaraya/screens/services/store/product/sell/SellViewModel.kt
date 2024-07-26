package com.anaraya.anaraya.screens.services.store.product.sell

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.screens.home.CategoryUiState
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Category
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class SellViewModel @Inject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) :
    ViewModel() {

    private val _sellUiState = MutableStateFlow(SellUiState())
    val sellUiState = _sellUiState as StateFlow<SellUiState>


    fun getAllData() {
        getCategories()
    }

    private fun getCategories() {
        _sellUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
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
        _sellUiState.update {
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
        _sellUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                sellCategoriesList = emptyList()
            )
        }
    }

    fun getSubCategories(categoryId: Int) {
        _sellUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                sellSubCategoriesList = emptyList()
            )
        }
        viewModelScope.launch {
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
        _sellUiState.update {
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
        _sellUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                sellSubCategoriesList = emptyList()
            )
        }
    }


    fun addItem(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        condition: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        isAnonymous: Boolean?,
        handleDelivery: Boolean?,
        productImage: File?,
        accept: Boolean,
    ) {
        resetErrors()
        _sellUiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        viewModelScope.launch {
            try {
                onAddItemSuccess(
                    manageStoreUseCase.storeAddProduct(
                        title,
                        categoryId,
                        subCategoryId,
                        condition,
                        itemDescription,
                        price,
                        location,
                        isAnonymous,
                        handleDelivery,
                        productImage,
                        accept,
                    )
                )
            } catch (e: NoInternetException) {
                onAddUserAddressFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddUserAddressFailure(e.message.toString())
            }
        }
    }


    private fun onAddItemSuccess(response: BaseResponse) {
        _sellUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                isSucceed = response.isSucceed,
                addProductMsg = response.message
            )
        }
    }


    private fun onAddUserAddressFailure(error: String) {
        when (error) {
            context.getString(R.string.title_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        titleError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.category_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        categoryError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.sub_category_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        subCategoryError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.condition_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        conditionError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.description_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        descriptionError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.price_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        priceError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.location_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        locationError = true,
                        focusError = true
                    )
                }
            }
            "Anonymous Field is Empty" -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        anonymousError = true,
                        focusError = true
                    )
                }
            }
            "Handle Delivery Field is Empty" -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        deliveryError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.image_field_is_empty) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        pictureError = true,
                        focusError = true
                    )
                }
            }

            context.getString(R.string.must_accept_terms_and_conditions) -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        conditionAndTermsError = true,
                        focusError = true
                    )
                }
            }

            else -> {
                _sellUiState.update {
                    it.copy(
                        isLoading = false,
                        error = error,
                        focusError = false
                    )
                }
            }
        }

    }

    fun resetErrors() {
        _sellUiState.update {
            it.copy(
                isLoading = false,
                titleError = false,
                categoryError = false,
                subCategoryError = false,
                conditionError = false,
                descriptionError = false,
                priceError = false,
                locationError = false,
                anonymousError = false,
                deliveryError = false,
                pictureError = false,
                conditionAndTermsError = false,
            )
        }
    }

    fun updateStateTermsAndCondition() {
        _sellUiState.update {
            it.copy(
                conditionAndTermsError = false,
            )
        }
    }

    fun resetMsg() {
        _sellUiState.update {
            it.copy(
                addProductMsg = null
            )
        }
    }
}