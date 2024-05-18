package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.MainCategory
import javax.inject.Inject

class MainCatPagingSource @Inject constructor(remoteDataSource: RemoteDataSource):BasePagingSource<MainCategory>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<MainCategory> {
        return remoteDataSource.getHomeCategory(page)
    }
}