package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.ExploreProduct
import com.anaraya.domain.entity.ExploreServices
import com.anaraya.domain.entity.Product
import javax.inject.Inject

class ExploreServicePagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val searchWord: String?,
    private val searchLang: String?,
    private val catIds: Int?,
    private val subCatId: Int?,
) : BasePagingSource<ExploreServices>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<ExploreServices> {
        return remoteDataSource.getExploreServices(page, searchWord, searchLang, catIds, subCatId)
    }
}