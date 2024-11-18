package com.kolown.data.datasource

import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.delay

interface GalleryDataSource {
    suspend fun getGalleryThumbnailList(userId: Long, page: Int, size: Int): List<GalleryThumbnail>
    suspend fun getGallery(id: Long): Gallery
}

class GalleryDataSourceFake : GalleryDataSource {
    override suspend fun getGallery(id: Long): Gallery {
        delay(1000)
        return Gallery(
            id = 1,
            postList = emptyList(),
            name = "name",
            description = "description"
        )

    }

    override suspend fun getGalleryThumbnailList(
        userId: Long,
        page: Int,
        size: Int
    ): List<GalleryThumbnail> {

    }
}
