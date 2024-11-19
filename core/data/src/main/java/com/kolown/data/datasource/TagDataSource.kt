package com.kolown.data.datasource

import com.kolown.model.Tag

interface TagDataSource {
    suspend fun getTagListByName(name: String): Result<List<Tag>>
}
