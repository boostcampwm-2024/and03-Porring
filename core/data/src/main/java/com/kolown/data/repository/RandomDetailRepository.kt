package com.kolown.data.repository

import com.kolown.model.ImageItem
import kotlinx.coroutines.flow.Flow

interface RandomDetailRepository {
   fun getItem(currentPage : Int) : Flow<List<ImageItem>>
}

class FakeRandomDetailRepository() : RandomDetailRepository {


    override fun getItem(currentPage: Int): Flow<List<ImageItem>> {
        TODO("Not yet implemented")
    }

}