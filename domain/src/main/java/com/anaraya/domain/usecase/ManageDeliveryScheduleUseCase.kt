package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageDeliveryScheduleUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getDeliverySchedule() = repo.getDeliverySchedule()
}