package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.Product
import javax.inject.Inject

class SearchPagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val searchWord: String,
    private val searchLang: String?,
    private val catIds: List<Int>?,
    private val brandIds: List<Int>?,
    private val highestOrLowestDiscount: Int? = null,
) : BasePagingSource<Product>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<Product> {
        return remoteDataSource.search(searchWord,searchLang,catIds,brandIds,highestOrLowestDiscount,page)
    }
}