package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.ProductStoreItemList
import javax.inject.Inject

class StoreProductServicePagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val stateId: Int,
    private val isProduct: Boolean,
) : BasePagingSource<ProductStoreItemList>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<ProductStoreItemList> {
        return remoteDataSource.getStoreProductAndService(page,stateId,isProduct)
    }
}