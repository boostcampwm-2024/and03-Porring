package com.kolown.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.paging.CommonPagingDataSource
import com.kolown.model.Tag
import javax.inject.Inject

class TagPagingDataSource(
    private val tagDataSource: TagDataSource,
    private val name: String
) : CommonPagingDataSource<Tag>() {

    //fake
    override fun providesNextKeyCondition(page: Int, body: List<Tag>): Boolean {
        return true
    }
    override suspend fun providePage(page: Int): Result<List<Tag>> {
        return tagDataSource.getTagListByName(name)
    }
}
