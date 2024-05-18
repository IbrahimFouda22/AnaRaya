package com.anaraya.domain.usecase

import com.anaraya.domain.entity.AddUpdateAddress
import com.anaraya.domain.entity.PlaceOrder
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageOrdersUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun placeOrder(paymentMethod: String) = repo.placeOrder(paymentMethod)
    suspend fun getOrders() = repo.getOrders()
}