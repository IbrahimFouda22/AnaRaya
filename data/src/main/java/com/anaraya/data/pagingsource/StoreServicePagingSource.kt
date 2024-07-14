package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.ServiceStoreItemList
import javax.inject.Inject

class StoreServicePagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val stateId: Int,
) : BasePagingSource<ServiceStoreItemList>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<ServiceStoreItemList> {
        return remoteDataSource.getStoreService(page,stateId)
    }
}