package com.kolown.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.di.Fake
import com.kolown.data.repository.GalleryRepository
import com.kolown.model.GalleryThumbnail
import javax.inject.Inject

class GalleryPagingDataSource @Inject constructor(
    @Fake private val galleryDataSource: GalleryDataSource,
    //임시
    private val userId: Long
) : PagingSource<Int, GalleryThumbnail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryThumbnail> {
        return try {
            val page = params.key ?: 0
            val result = galleryDataSource.getGalleryThumbnailList(userId, page)
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

    override fun getRefreshKey(state: PagingState<Int, GalleryThumbnail>): Int? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }


}
