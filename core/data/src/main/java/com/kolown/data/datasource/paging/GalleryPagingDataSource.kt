package com.kolown.data.datasource.paging

import com.kolown.data.datasource.fake.GalleryDataSource
import com.kolown.data.di.Fake
import com.kolown.model.GalleryThumbnail
import javax.inject.Inject

class GalleryPagingDataSource @Inject constructor(
    @Fake private val galleryDataSource: GalleryDataSource,
    //임시
    private val userId: Long,
) : CommonPagingDataSource<GalleryThumbnail>() {
    override suspend fun providePage(page: Int): Result<List<GalleryThumbnail>> {
        return galleryDataSource.getGalleryThumbnailList(userId, page)
    }


}
