package com.anaraya.anaraya.home.services.store.explore

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.home.home.CategoryUiState
import com.anaraya.domain.entity.Category
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val manageStoreUseCase: ManageStoreUseCase,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _exploreUiState = MutableStateFlow(ExploreUiState())
    val exploreUiState = _exploreUiState as StateFlow<ExploreUiState>



    fun getAllData(isProduct: Boolean) {
        getExploreCategories(isProduct)
    }

    private fun getExploreCategories(isProduct: Boolean) {
        _exploreUiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetExploreCategoriesSuccess(manageStoreUseCase.getStoreCategories(isProduct))
            } catch (e: NoInternetException) {
                onGetExploreCategoriesFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetExploreCategoriesFailure(e.message.toString())
            }
        }
    }

    private fun onGetExploreCategoriesSuccess(response: List<Category>) {
        _exploreUiState.update {
            it.copy(
                isLoading = false,
                error = null,
                exploreCategoriesList = response.map { data ->
                    CategoryUiState(
                        id = data.id,
                        name = data.name,
                        image = null
                    )
                }
            )
        }
    }

    private fun onGetExploreCategoriesFailure(error: String) {
        _exploreUiState.update {
            it.copy(
                isLoading = false,
                error = error,
                exploreCategoriesList = emptyList()
            )
        }
    }
//    fun navigateToFeedBack() {
//        _exploreUiState.update {
//            it.copy(
//                navigateToFeedBack = true
//            )
//        }
//    }
//
//    fun navigateToFeedBackDone() {
//        _exploreUiState.update {
//            it.copy(
//                navigateToFeedBack = false
//            )
//        }
//    }

}