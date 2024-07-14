package com.anaraya.domain.usecase

import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageProductUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getProductsAds() = repo.getProductsAds()
    suspend fun getProductById(productId: Int) = repo.getProductById(productId)
    suspend fun getProductsByCategory(catId: Int) =
        repo.getProductsByCategory(catId)
    suspend fun getProductsByMainCategory(mainCatId: Int) =
        repo.getProductsByMainCategory(mainCatId)
    suspend fun getProductsByBrand(brandId: Int) =
        repo.getProductsByBrand(brandId)
    suspend fun getProductsByMainCatAndCat(mainCatId: Int, catId: Int) =
        repo.getProductsInsideMainCatAndCat(categoryId = catId, mainCatId =  mainCatId)

    suspend fun getTrendingProducts() = repo.getTrendingProducts()
    suspend fun getPointsProducts() = repo.getPointsProducts()
    suspend fun getAll() = repo.getAll()
    suspend fun getAllProduct() = repo.getAllProduct()
    suspend fun addToNotifyMe(productId: Int) = repo.addToNotifyMe(productId)
    suspend fun removeFromNotifyMe(productId: Int) = repo.removeFromNotifyMe(productId)

    suspend fun search(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
    ) = repo.search(searchWord, searchLanguage, catIds, brandIds, highestOrLowest)
}