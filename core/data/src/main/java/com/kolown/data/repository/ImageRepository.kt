package com.kolown.data.repository

import com.kolown.model.ImageItem
import com.kolown.model.Reactions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeImageRepository

interface ImageRepository {
    fun getItemByPage(currentPage: Int): Flow<List<ImageItem>>
    fun getItems(): Flow<List<ImageItem>>
}

@FakeImageRepository
class FakeImageRepositoryImpl : ImageRepository {
    private val mockData = listOf(
        ImageItem(
            id = 1,
            imageUrl = "https://i0.wp.com/picjumbo.com/wp-content/uploads/beautiful-nature-mountain-scenery-with-flowers-free-photo.jpg?w=2210&quality=70",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ),
        ImageItem(
            id = 1,
            imageUrl = "https://media.istockphoto.com/id/517188688/ko/%EC%82%AC%EC%A7%84/%EC%82%B0-%ED%92%8D%EA%B2%BD.jpg?s=1024x1024&w=0&k=20&c=DiXclovmh6FCUoEn59Wb9qqDaB98ixT8dsSpxpwrlZk=",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ),
        ImageItem(
            id = 1,
            imageUrl = "https://i0.wp.com/picjumbo.com/wp-content/uploads/beautiful-nature-mountain-scenery-with-flowers-free-photo.jpg?w=2210&quality=70",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ),
        ImageItem(
            id = 1,
            imageUrl = "https://media.istockphoto.com/id/517188688/ko/%EC%82%AC%EC%A7%84/%EC%82%B0-%ED%92%8D%EA%B2%BD.jpg?s=1024x1024&w=0&k=20&c=DiXclovmh6FCUoEn59Wb9qqDaB98ixT8dsSpxpwrlZk=",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ), ImageItem(
            id = 1,
            imageUrl = "https://media.istockphoto.com/id/484270482/ko/%EC%82%AC%EC%A7%84/%EC%B6%94%EC%A0%88-in-%EB%AF%B8%EC%8B%9C%EC%98%A4%EB%84%A4%EC%8A%A4.jpg?s=612x612&w=0&k=20&c=j7sxiMk1mzDsLUG-sSqX-32I9kv4vK9CxdQsI9cWolw=",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ), ImageItem(
            id = 1,
            imageUrl = "https://media.istockphoto.com/id/484270482/ko/%EC%82%AC%EC%A7%84/%EC%B6%94%EC%A0%88-in-%EB%AF%B8%EC%8B%9C%EC%98%A4%EB%84%A4%EC%8A%A4.jpg?s=612x612&w=0&k=20&c=j7sxiMk1mzDsLUG-sSqX-32I9kv4vK9CxdQsI9cWolw=",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ), ImageItem(
            id = 1,
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTP2HagVH6yxTJZEwHLkARfhEtWZhE2N1iSYw&s",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ), ImageItem(
            id = 1,
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTP2HagVH6yxTJZEwHLkARfhEtWZhE2N1iSYw&s",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        ), ImageItem(
            id = 1,
            imageUrl = "https://media.istockphoto.com/id/517188688/ko/%EC%82%AC%EC%A7%84/%EC%82%B0-%ED%92%8D%EA%B2%BD.jpg?s=1024x1024&w=0&k=20&c=DiXclovmh6FCUoEn59Wb9qqDaB98ixT8dsSpxpwrlZk=",
            user = "hi",
            content = "화이팅",
            reactions = Reactions.STAR,
            isFollowed = true,
            favoriteList = listOf()
        )
    )

    override fun getItemByPage(currentPage: Int): Flow<List<ImageItem>> = flow {
        emit(mockData.subList(currentPage * 5, (currentPage + 1) * 5))
    }

    override fun getItems(): Flow<List<ImageItem>> = flow {
        emit(mockData)
    }
}