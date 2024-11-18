package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.datasource.GalleryPagingDataSource
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

interface GalleryRepository {
    suspend fun getGalleryThumbnailList(userId: Long, page: Int): Result<List<GalleryThumbnail>>
    suspend fun getGallery(id: Long): Result<Gallery>
    suspend fun getGalleryThumbnailPagingFlow(userId: Long): Flow<PagingData<GalleryThumbnail>>
}

class FakeGalleryRepository @Inject constructor(
    private val dataSourceFake: FakeGalleryDataSource,
) :
    GalleryRepository {
    override suspend fun getGalleryThumbnailList(
        userId: Long,
        page: Int,
    ): Result<List<GalleryThumbnail>> {

        if (Random.nextInt(3) == 0)
            return Result.failure(Exception("테스트 에러 발생"))
        return dataSourceFake.getGalleryThumbnailList(userId, page)
    }

    override suspend fun getGallery(id: Long): Result<Gallery> {
        if (Random.nextInt(3) == 0)
            return Result.failure(Exception("테스트 에러 발생"))
        return dataSourceFake.getGallery(id)
    }

    override suspend fun getGalleryThumbnailPagingFlow(userId: Long): Flow<PagingData<GalleryThumbnail>> {
        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = { GalleryPagingDataSource(dataSourceFake, userId) }).flow
    }

    companion object {
        const val PAGE_SIZE = 40
    }
}
