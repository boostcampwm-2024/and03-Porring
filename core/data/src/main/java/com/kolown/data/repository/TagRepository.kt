package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.FakeTagDataSource
import com.kolown.data.datasource.TagDataSource
import com.kolown.data.datasource.TagPagingDataSource
import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Tag
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TagRepository {
    suspend fun getTagListByName(name: String): Result<List<Tag>>
    suspend fun getTagePageFlow(name: String): Flow<PagingData<Tag>>
}

class TagRepositoryImpl(private val tagDataSource: TagDataSource) : TagRepository {
    override suspend fun getTagListByName(name: String): Result<List<Tag>> {
        return tagDataSource.getTagListByName(name)
    }

    override suspend fun getTagePageFlow(name: String): Flow<PagingData<Tag>> = flow {
        while (true) {
            val result = getTagListByName(name)
            if (result.isSuccess) {
                emit(PagingData.from(result.getOrThrow()))
            }
        }
    }
}

class FakeTagRepository() : TagRepository {
    private val tagDataSource = FakeTagDataSource()
    override suspend fun getTagListByName(name: String): Result<List<Tag>> {
        delay(500L)
        return Result.success(MockDataProvider.getTagByName(name))
    }

    override suspend fun getTagePageFlow(name: String): Flow<PagingData<Tag>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { TagPagingDataSource(tagDataSource, name) }
        ).flow
}
