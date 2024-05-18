package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageAboutUsUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getAboutUs() = repo.getAboutUs()

}