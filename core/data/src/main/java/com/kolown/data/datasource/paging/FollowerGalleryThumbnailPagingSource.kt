package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.fake.FollowerDataSource
import com.kolown.model.FollowerThumbnail

class FollowerGalleryThumbnailPagingDataSource(
    private val followerDataSource: FollowerDataSource,
) : PagingSource<Int, FollowerThumbnail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FollowerThumbnail> {
        return try {
            val page = params.key ?: 0
            val result = followerDataSource.getFollowerThumbnailAlbum(page)
            val body = result.getOrElse {
                return LoadResult.Error(it)
            }
            LoadResult.Page(
                data = body,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (body.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FollowerThumbnail>): Int? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}