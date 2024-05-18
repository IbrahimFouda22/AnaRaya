package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManagePromoUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getAllPromoCodes() = repo.getAllPromoCodes()
    suspend fun applyPromo(promoCode: String) = repo.applyPromo(promoCode)

}