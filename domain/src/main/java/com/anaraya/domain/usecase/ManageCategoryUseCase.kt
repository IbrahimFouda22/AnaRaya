package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageCategoryUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getHomeCategory() = repo.getHomeCategory()

    suspend fun getAllCats() = repo.getAllCats()
    suspend fun getAllCatsInsideMainCat(mainCatId: Int) = repo.getAllCategoryInsideMainCat(mainCatId)
    suspend fun getAllBrands() = repo.getAllBrands()
}