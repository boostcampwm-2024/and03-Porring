package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.datasource.paging.GalleryPagingDataSource
import com.kolown.data.di.Fake
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

interface GalleryRepository {
    suspend fun getGalleryThumbnailList(userId: Long, page: Int): Result<List<GalleryThumbnail>>
    suspend fun getGallery(id: Long): Result<Gallery>
    fun getGalleryThumbnailPagingFlow(userId: Long): Flow<PagingData<GalleryThumbnail>>
}

class GalleryRepositoryImpl @Inject constructor(
    @Fake
    private val dataSource: GalleryDataSource,
) : GalleryRepository {
    override suspend fun getGalleryThumbnailList(
        userId: Long,
        page: Int,
    ): Result<List<GalleryThumbnail>> {

        if (Random.nextInt(3) == 0)
            return Result.failure(Exception("테스트 에러 발생"))
        return dataSource.getGalleryThumbnailList(userId, page)
    }

    override suspend fun getGallery(id: Long): Result<Gallery> {
        if (Random.nextInt(3) == 0)
            return Result.failure(Exception("테스트 에러 발생"))
        return dataSource.getGallery(id)
    }

    override fun getGalleryThumbnailPagingFlow(userId: Long): Flow<PagingData<GalleryThumbnail>> {
        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = { GalleryPagingDataSource(dataSource, userId) }).flow
    }

    companion object {
        const val PAGE_SIZE = 40
    }
}
