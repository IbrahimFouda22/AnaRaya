package com.anaraya.anaraya.home.more.feedback.question2

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaraya.anaraya.R
import com.anaraya.domain.entity.FeedBack
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.usecase.ManageFeedBackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Question2ViewModel @SuppressLint("StaticFieldLeak")
@Inject constructor(
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val manageFeedBackUseCase: ManageFeedBackUseCase
) : ViewModel() {

    private val _question2UiState = MutableStateFlow(Question2UiState())
    val question2UiState = _question2UiState as StateFlow<Question2UiState>

    init {
        getFeedBackQuestion()
    }

    fun getAllData(){
        getFeedBackQuestion()
    }
    private fun getFeedBackQuestion() {
        _question2UiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onGetFeedBackQuestionSuccess(manageFeedBackUseCase.getFeedBackQuestion())
            } catch (e: NoInternetException) {
                onGetFeedBackQuestionFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onGetFeedBackQuestionFailure(e.message.toString())
            }
        }
    }

    private fun onGetFeedBackQuestionSuccess(response: FeedBack) {
        _question2UiState.update {
            it.copy(
                isLoading = false,
                error = null,
                message = response.message,
                question = response.data,
                isSucceed = response.isSucceed,
            )
        }
    }

    private fun onGetFeedBackQuestionFailure(error: String) {
        _question2UiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceed = false,
                question = null,
                message = null
            )
        }
    }

    fun addFeedBack(rating: Int, review: String?) {
        _question2UiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            try {
                onAddFeedBackSuccess(manageFeedBackUseCase.addFeedBack(rating, review))
            } catch (e: NoInternetException) {
                onAddFeedBackFailure(context.getString(R.string.no_internet))
            } catch (e: Exception) {
                onAddFeedBackFailure(e.message.toString())
            }
        }
    }

    private fun onAddFeedBackSuccess(response: FeedBack) {
        _question2UiState.update {
            it.copy(
                isLoading = false,
                error = null,
                messageAddFeedBack = response.message,
                isSucceedAddFeedBack = response.isSucceed,
            )
        }
    }

    private fun onAddFeedBackFailure(error: String) {
        _question2UiState.update {
            it.copy(
                isLoading = false,
                error = error,
                isSucceedAddFeedBack = false,
                messageAddFeedBack = null
            )
        }
    }

}