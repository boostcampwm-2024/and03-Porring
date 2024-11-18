package com.kolown.data.repository

import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail

interface GalleryRepository {
    suspend fun getGalleryThumbnailList(userId:Long, page:Int, size:Int):List<GalleryThumbnail>
    suspend fun getGallery(id: Long):Gallery
}
