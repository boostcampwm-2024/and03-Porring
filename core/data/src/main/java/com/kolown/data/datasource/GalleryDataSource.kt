package com.kolown.data.datasource

import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.delay
import kotlin.random.Random

interface GalleryDataSource {
    suspend fun getGalleryThumbnailList(userId: Long, page: Int): Result<List<GalleryThumbnail>>
    suspend fun getGallery(id: Long): Result<Gallery>
}

class FakeGalleryDataSource : GalleryDataSource {
    private var test = 0
    override suspend fun getGallery(id: Long): Result<Gallery> {
        delay(500)
        return Result.success(MockDataProvider.getRandomGallery())
    }

    override suspend fun getGalleryThumbnailList(
        userId: Long,
        page: Int,
    ): Result<List<GalleryThumbnail>> {
        delay(if (test == 0) 500 else 5000)
        test++
        if (Random.nextInt(4) == 3) {
            return Result.failure(Exception())
        }
        return Result.success(MockDataProvider.getRandomGalleryThumbnailList())
    }

}
