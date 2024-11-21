package com.kolown.data.datasource

import com.kolown.data.datasource.paging.CommonPagingDataSource
import com.kolown.model.Post
import com.kolown.model.Tag

class PostPagingDataSource(
    private val postDataSource: PostDataSource,
    private val tag: Tag
) : CommonPagingDataSource<Post>() {
    override suspend fun providePage(page: Int): Result<List<Post>> {
        return postDataSource.getPostListByTag(tag)
    }
}
