package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageFavUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun addToFav(
        productId: Int,
    ) = repo.addToFav(productId)

    suspend fun deleteFromFav(
        productId: Int,
    ) = repo.deleteFromFav(productId)

    suspend fun getAllFavorite() = repo.getAllFavorite()
}