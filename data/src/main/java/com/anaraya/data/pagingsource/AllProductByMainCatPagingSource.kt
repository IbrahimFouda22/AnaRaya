package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.Product
import javax.inject.Inject

class AllProductByMainCatPagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val categoryId: Int
) : BasePagingSource<Product>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<Product> {
        return remoteDataSource.getProductsByMainCategory(categoryId,page)
    }
}