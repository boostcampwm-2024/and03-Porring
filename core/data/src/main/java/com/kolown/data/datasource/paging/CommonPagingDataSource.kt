package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class CommonPagingDataSource<T : Any> : PagingSource<Int, T>() {

    protected abstract suspend fun providePage(page: Int): Result<List<T>>

    open fun providesNextKeyCondition(page: Int, body: List<T>) = body.isEmpty()
    open fun providesPrevKeyCondition(page: Int, body: List<T>) = page == 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 0
            val result = providePage(page)
            val body = result.getOrElse {
                return LoadResult.Error(it)
            }
            LoadResult.Page(
                data = body,
                prevKey = if (providesPrevKeyCondition(page, body)) null else page - 1,
                nextKey = if (providesNextKeyCondition(page, body)) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}
