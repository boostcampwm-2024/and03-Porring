package com.kolown.data.datasource.fake

import com.kolown.data.mock.MockDataProvider
import com.kolown.model.FollowerThumbnail
import kotlinx.coroutines.delay
import javax.inject.Inject

interface FollowerDataSource {
//    suspend fun getFollowerThumbnailAlbum(page: Int): Result<List<FollowerThumbnail>>
}

class FakeFollowerDataSource @Inject constructor() : FollowerDataSource {
    private var test = 0
//    override suspend fun getFollowerThumbnailAlbum(
//        page: Int,
//    ): Result<List<FollowerThumbnail>> {
//        delay(3000)
//        return Result.success<List<FollowerThumbnail>>(MockDataProvider.getFollowerAlbums())
//    }
}