package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageContactNumberUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getContactNumber() = repo.getSupportContactNumber()

}