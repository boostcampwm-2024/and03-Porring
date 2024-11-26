package com.kolown.data.datasource.fake

import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Tag
import kotlin.random.Random

interface TagDataSource {
    suspend fun getTagListByName(name: String): Result<List<Tag>>
}

class FakeTagDataSource() : TagDataSource {
    override suspend fun getTagListByName(name: String): Result<List<Tag>> {
        if (Random.nextInt(3) == 0) {
            return Result.failure(Exception())
        }
        return Result.success(MockDataProvider.getTagByName(name))
    }
}
