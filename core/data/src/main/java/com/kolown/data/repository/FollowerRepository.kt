package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.FollowerDataSource
import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.datasource.paging.FollowerGalleryThumbnailPagingDataSource
import com.kolown.model.FollowerThumbnail
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

interface FollowerRepository {
    fun getFollowerDataSourcePagingFlow(userId: Long): Flow<PagingData<FollowerThumbnail>>
}

class FollowerRepositoryImpl @Inject constructor(
    private val dataSource: FollowerDataSource,
) : FollowerRepository {

    override fun getFollowerDataSourcePagingFlow(userId: Long): Flow<PagingData<FollowerThumbnail>> {
        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = { FollowerGalleryThumbnailPagingDataSource(dataSource) }).flow
    }

    companion object {
        const val PAGE_SIZE = 40
    }
}