package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.Product
import javax.inject.Inject

class PointsProductPagingSource @Inject constructor(remoteDataSource: RemoteDataSource):BasePagingSource<Product>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<Product> {
        return remoteDataSource.getPointsProducts(page)
    }
}