package com.kolown.data.datasource.paging

import com.kolown.data.datasource.fake.TagDataSource
import com.kolown.model.Tag

class TagPagingDataSource(
    private val tagDataSource: TagDataSource,
    private val name: String,
) : CommonPagingDataSource<Tag>() {

    //fake
    override fun providesNextKeyCondition(page: Int, body: List<Tag>): Boolean {
        return true
    }

    override suspend fun providePage(page: Int): Result<List<Tag>> {
        return tagDataSource.getTagListByName(name)
    }
}
