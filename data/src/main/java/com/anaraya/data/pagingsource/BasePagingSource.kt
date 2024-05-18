package com.anaraya.data.pagingsource

import android.content.res.Resources.NotFoundException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.exception.NoInternetException
import java.net.SocketException

abstract class BasePagingSource<value : Any>(protected val remoteDataSource: RemoteDataSource) :
    PagingSource<Int, value>() {
    protected abstract suspend fun fetchData(page: Int): List<value>
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, value> {
        val currentPage = params.key ?: 1
        return try {
            val response = fetchData(currentPage)
            val nextKey = (currentPage + 1).takeIf { response.lastIndex >= currentPage }
            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (e: NoInternetException) {
            LoadResult.Error(e)
        } catch (e: SocketException) {
            LoadResult.Error(e)
        } catch (e: NotFoundException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, value>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}