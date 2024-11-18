package com.kolown.data.datasource

import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.delay

interface GalleryDataSource {
    suspend fun getGalleryThumbnailList(userId: Long, page: Int): Result<List<GalleryThumbnail>>
    suspend fun getGallery(id: Long): Result<Gallery>
}

class FakeGalleryDataSource : GalleryDataSource {
    override suspend fun getGallery(id: Long): Result<Gallery>{
        delay(500)
        return Result.success(MockDataProvider.getRandomGallery())
    }

    override suspend fun getGalleryThumbnailList(
        userId: Long,
        page: Int,
    ): Result<List<GalleryThumbnail>>{
        delay(500)
        return Result.success(MockDataProvider.getRandomGalleryThumbnailList())
    }

}
