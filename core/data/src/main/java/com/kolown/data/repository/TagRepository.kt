package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.paging.TagPagingSource
import com.kolown.network.TagDataSource
import com.kolown.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TagRepository {
    suspend fun getTagBySearch(search: String): Flow<PagingData<Tag>>
}

class TagRepositoryImpl  @Inject constructor(private val tagDataSource: TagDataSource) : TagRepository {

    override suspend fun getTagBySearch(search: String): Flow<PagingData<Tag>> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { TagPagingSource(searchText = search, tagDataSource = tagDataSource) } // PagingSource 제공
        ).flow
    }
}
