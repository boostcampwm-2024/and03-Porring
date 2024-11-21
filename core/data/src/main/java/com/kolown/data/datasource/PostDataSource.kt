package com.kolown.data.datasource

import com.kolown.data.mock.MockDataProvider
import com.kolown.model.Post
import com.kolown.model.Tag

interface PostDataSource {
    suspend fun getPostListByTag(tag: Tag): Result<List<Post>>
}

class FakePostDataSource():PostDataSource{
    override suspend fun getPostListByTag(tag: Tag): Result<List<Post>> {
        return Result.success(MockDataProvider.getRandomPostList())
    }
}
