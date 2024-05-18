package com.anaraya.domain.usecase

import com.anaraya.domain.entity.AddAllToBasket
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageCartUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun addCart(
        productId: Int,
        productQty: Int
    ) =repo.addToCart(productId, productQty)

    suspend fun getCart() = repo.getCart()
    suspend fun removeProductFromCart(productId: Int) = repo.removeProductFromCart(productId)

    suspend fun getCheckOut() = repo.checkOut()
    suspend fun addAllToBasket() = repo.addAllToBasket()

}