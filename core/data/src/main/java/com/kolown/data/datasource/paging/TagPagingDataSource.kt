package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.model.Tag

class TagPagingSource(
    private val searchText: String,
    private val tagDataSource: TagDataSource
) : PagingSource<String, Tag>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Tag> {
        return try {
            val page = params.key
            val tags = tagDataSource.getTagBySearch(searchText = searchText,key = params.key, perPage = params.loadSize.toLong()).getOrElse {
                throw Exception("태그 불러오기 실패")
            }
            val nextKey = if (tags.isEmpty()) null else tags.last().name

            LoadResult.Page(
                data = tags,
                prevKey = if (page == null) null else tags.firstOrNull()?.name,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Tag>): String? {
        return null
    }


}