package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.CompanyAddressItem
import javax.inject.Inject

class AllCompanyAddressPagingSource @Inject constructor(
    remoteDataSource: RemoteDataSource,
    private val companyId: Int,
    private val governorate: String,
) : BasePagingSource<CompanyAddressItem>(remoteDataSource) {
    override suspend fun fetchData(page: Int): List<CompanyAddressItem> {
        return remoteDataSource.getAllCompanyAddresses(page,companyId, governorate).data.itemsList
    }
}