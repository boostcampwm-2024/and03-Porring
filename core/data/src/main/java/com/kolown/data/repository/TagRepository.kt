package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.fake.FakeTagDataSource
import com.kolown.data.datasource.paging.TagPagingSource
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Tag
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TagRepository {
    suspend fun getTagBySearch(search: String): Flow<PagingData<Tag>>
}

class TagRepositoryImpl(private val tagDataSource: TagDataSource) : TagRepository {

    override suspend fun getTagBySearch(search: String): Flow<PagingData<Tag>> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { TagPagingSource(searchText = search, tagDataSource = tagDataSource) } // PagingSource 제공
        ).flow
    }
}
