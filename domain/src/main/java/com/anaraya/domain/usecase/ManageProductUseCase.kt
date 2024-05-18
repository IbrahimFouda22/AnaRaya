package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageProductUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getProductsAds() = repo.getProductsAds()
    suspend fun getProductById(productId: Int) = repo.getProductById(productId)
    suspend fun getProductsByMainCategory(mainCatId: Int) =
        repo.getProductsByMainCategory(mainCatId)

    suspend fun getProductsInsideMainCatAndCat(categoryId: Int, mainCatId: Int) =
        repo.getProductsInsideMainCatAndCat(categoryId, mainCatId)

    //    suspend fun getProductsByCategory(categoryId: Int) = repo.getProductsByCategory(categoryId)
    suspend fun getTrendingProducts() = repo.getTrendingProducts()
    suspend fun getAll() = repo.getAll()
    suspend fun getAllProduct() = repo.getAllProduct()

    suspend fun search(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
    ) = repo.search(searchWord, searchLanguage, catIds, brandIds, highestOrLowest)
}