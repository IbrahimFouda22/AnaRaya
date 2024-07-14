package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageInfoUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getAboutUs() = repo.getAboutUs()
    suspend fun getTermsAndConditions() = repo.getTermsAndCondition()
    suspend fun getPrivacyPolicy() = repo.getPrivacyPolicy()

}