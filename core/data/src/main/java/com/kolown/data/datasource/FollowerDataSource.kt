package com.kolown.data.datasource

import com.kolown.data.mock.MockDataProvider
import com.kolown.model.FollowerThumbnail
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

interface FollowerDataSource {
    suspend fun getFollowerThumbnailAlbum(page: Int): Result<List<FollowerThumbnail>>
}

class FakeFollowerDataSource @Inject constructor(): FollowerDataSource {
    private var test = 0
    override suspend fun getFollowerThumbnailAlbum(
        page: Int
    ): Result<List<FollowerThumbnail>> {
        delay(3000)
        return Result.success<List<FollowerThumbnail>>(MockDataProvider.getFollowerAlbums())
    }
}