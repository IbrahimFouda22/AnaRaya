package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.ExploreProduct
import com.anaraya.domain.entity.Product
import javax.inject.Inject

class ExploreProductPagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val searchWord: String?,
    private val searchLang: String?,
    private val catIds: Int?,
    private val subCatId: Int?,
) : BasePagingSource<ExploreProduct>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<ExploreProduct> {
        return remoteDataSource.getExploreProducts(page, searchWord, searchLang, catIds, subCatId)
    }
}