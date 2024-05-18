package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageFeedBackUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getFeedBackQuestion() = repo.getFeedBackQuestion()
    suspend fun addFeedBack(rating: Int, review: String?) = repo.addFeedBack(rating, review)
}