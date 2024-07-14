package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.ProductStore
import javax.inject.Inject

class StoreProductPagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val stateId: Int,
) : BasePagingSource<ProductStore>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<ProductStore> {
        return remoteDataSource.getStoreProduct(page,stateId)
    }
}