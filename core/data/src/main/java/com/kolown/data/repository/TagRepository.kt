package com.kolown.data.repository

import com.kolown.data.datasource.TagDataSource
import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Tag
import kotlinx.coroutines.delay

interface TagRepository {
    suspend fun getTagListByName(name: String): Result<List<Tag>>
}

class TagRepositoryImpl(private val tagDataSource: TagDataSource) : TagRepository {
    override suspend fun getTagListByName(name: String): Result<List<Tag>> {
        return tagDataSource.getTagListByName(name)
    }
}

class FakeTagRepository() : TagRepository {
    override suspend fun getTagListByName(name: String): Result<List<Tag>> {
        delay(500L)
        return Result.success(MockDataProvider.getTagByName(name))
    }
}
