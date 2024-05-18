package com.anaraya.data.pagingsource

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.Cart
import javax.inject.Inject

//class CartPagingSource @Inject constructor(remoteDataSource: RemoteDataSource):BasePagingSource<Cart>(remoteDataSource) {
//    override suspend fun fetchData(page: Int): List<Cart> {
//        return remoteDataSource.getCart(page)
//    }
//}