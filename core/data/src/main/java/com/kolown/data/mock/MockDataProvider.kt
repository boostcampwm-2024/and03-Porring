package com.kolown.data.mock

import android.util.Log
import com.kolown.model.FollowerThumbnail
import com.kolown.model.Gallery
import com.kolown.model.GalleryThumbnail
import com.kolown.model.Post
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.Tag

object MockDataProvider {
    private val imageUrlList = listOf(
        "https://i0.wp.com/picjumbo.com/wp-content/uploads/beautiful-nature-mountain-scenery-with-flowers-free-photo.jpg?w=2210&quality=70",
        "https://media.istockphoto.com/id/517188688/ko/%EC%82%AC%EC%A7%84/%EC%82%B0-%ED%92%8D%EA%B2%BD.jpg?s=1024x1024&w=0&k=20&c=DiXclovmh6FCUoEn59Wb9qqDaB98ixT8dsSpxpwrlZk=",
        "https://media.istockphoto.com/id/484270482/ko/%EC%82%AC%EC%A7%84/%EC%B6%94%EC%A0%88-in-%EB%AF%B8%EC%8B%9C%EC%98%A4%EB%84%A4%EC%8A%A4.jpg?s=612x612&w=0&k=20&c=j7sxiMk1mzDsLUG-sSqX-32I9kv4vK9CxdQsI9cWolw=",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTP2HagVH6yxTJZEwHLkARfhEtWZhE2N1iSYw&s",
        "https://i.pinimg.com/474x/b1/2a/82/b12a82a85510ee065ad885d90ca2063c.jpg",
        "https://jnilbo.com/upimages/gisaimg/202409/22_748973-75.jpg",
        "https://cdn.pixabay.com/photo/2019/10/13/13/19/landscape-4546121_960_720.jpg",
    )

    private val descriptionList = listOf(
        "무니 디스크립션",
        "영수 디스크립션",
        "용수 디스크립션",
        "하현 디스크립션",
        "매우매우 긴 디스크립션 매우매우 긴 디스크립션 매우매우 긴 디스크립션 매우매우 긴 디스크립션 매우매우 긴 디스크립션 매우매우 긴 디스크립션 매우매우 긴 디스크립션"
    )

    private val nameList = listOf(
        "무니",
        "영수",
        "용수",
        "하현",
        "현석",
        "아이비",
        "태환"
    )

    private val tagNameList = listOf(
        "더미 태그 풍경",
        "더미 태그 인물",
        "더미 태그 여행",
        "더미 태그 겨율",
        "매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그 매우 긴 태그"
    )

//    private val followers = listOf(
//        FollowerThumbnail(
//            id = "1",
//            followerName = "하현",
//            images = listOf(
//                "https://cdn.pixabay.com/photo/2023/02/01/10/37/sunset-7760143_1280.jpg",
//                "https://img.freepik.com/free-photo/beautiful-morning-pang-ung-lake-pang-ung-mae-hong-son-province-thailand_335224-935.jpg",
//                "https://png.pngtree.com/thumb_back/fh260/background/20220313/pngtree-photography-of-night-scenery-of-galaxy-starry-sky-image_1000896.jpg",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvm8rWyVx0xDvA6ZdRGM1a6UWlrqyLdhNaQM3Prrj5rvHV8MX4Ms3tQbERS3HXO4DFns0&usqp=CAU"
//            )
//        ),
//        FollowerThumbnail(
//            id = "2",
//            followerName = "용수",
//            images = listOf(
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvm8rWyVx0xDvA6ZdRGM1a6UWlrqyLdhNaQM3Prrj5rvHV8MX4Ms3tQbERS3HXO4DFns0&usqp=CAU",
//                "https://cdn.pixabay.com/photo/2023/02/01/10/37/sunset-7760143_1280.jpg",
//                "https://img.freepik.com/free-photo/beautiful-morning-pang-ung-lake-pang-ung-mae-hong-son-province-thailand_335224-935.jpg",
//                "https://png.pngtree.com/thumb_back/fh260/background/20220313/pngtree-photography-of-night-scenery-of-galaxy-starry-sky-image_1000896.jpg"
//            )
//        ),
//        FollowerThumbnail(
//            id = "1",
//            followerName = "문휘",
//            images = listOf(
//                "https://cdn.pixabay.com/photo/2023/02/01/10/37/sunset-7760143_1280.jpg",
//                "https://png.pngtree.com/thumb_back/fh260/background/20220313/pngtree-photography-of-night-scenery-of-galaxy-starry-sky-image_1000896.jpg",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvm8rWyVx0xDvA6ZdRGM1a6UWlrqyLdhNaQM3Prrj5rvHV8MX4Ms3tQbERS3HXO4DFns0&usqp=CAU",
//                "https://img.freepik.com/free-photo/beautiful-morning-pang-ung-lake-pang-ung-mae-hong-son-province-thailand_335224-935.jpg"
//            )
//        ),
//        FollowerThumbnail(
//            id = "1",
//            followerName = "영수",
//            images = listOf(
//                "https://png.pngtree.com/thumb_back/fh260/background/20220313/pngtree-photography-of-night-scenery-of-galaxy-starry-sky-image_1000896.jpg",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvm8rWyVx0xDvA6ZdRGM1a6UWlrqyLdhNaQM3Prrj5rvHV8MX4Ms3tQbERS3HXO4DFns0&usqp=CAU",
//                "https://img.freepik.com/free-photo/beautiful-morning-pang-ung-lake-pang-ung-mae-hong-son-province-thailand_335224-935.jpg",
//                "https://cdn.pixabay.com/photo/2023/02/01/10/37/sunset-7760143_1280.jpg"
//            )
//        ),
//        FollowerThumbnail(
//            id = "2",
//            followerName = "아이비",
//            images = listOf(
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvm8rWyVx0xDvA6ZdRGM1a6UWlrqyLdhNaQM3Prrj5rvHV8MX4Ms3tQbERS3HXO4DFns0&usqp=CAU",
//                "https://cdn.pixabay.com/photo/2023/02/01/10/37/sunset-7760143_1280.jpg",
//                "https://img.freepik.com/free-photo/beautiful-morning-pang-ung-lake-pang-ung-mae-hong-son-province-thailand_335224-935.jpg",
//                "https://png.pngtree.com/thumb_back/fh260/background/20220313/pngtree-photography-of-night-scenery-of-galaxy-starry-sky-image_1000896.jpg"
//            )
//        ),
//        FollowerThumbnail(
//            id = "2",
//            followerName = "JK",
//            images = listOf(
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvm8rWyVx0xDvA6ZdRGM1a6UWlrqyLdhNaQM3Prrj5rvHV8MX4Ms3tQbERS3HXO4DFns0&usqp=CAU",
//                "https://cdn.pixabay.com/photo/2023/02/01/10/37/sunset-7760143_1280.jpg",
//                "https://img.freepik.com/free-photo/beautiful-morning-pang-ung-lake-pang-ung-mae-hong-son-province-thailand_335224-935.jpg",
//                "https://png.pngtree.com/thumb_back/fh260/background/20220313/pngtree-photography-of-night-scenery-of-galaxy-starry-sky-image_1000896.jpg"
//            )
//        ),
//    )


    fun getRandomName(): String {
        return nameList.random()
    }

    fun getRandomId(): Long {
        return (1..1000).random().toLong()
    }

    fun getRandomDescription(): String {
        return descriptionList.random()
    }

    fun getRandomImageUrl(): String {
        return imageUrlList.random()
    }

    fun getRandomTagName(): String {
        return tagNameList.random()
    }

    fun getRandomTag(): Tag {
        return Tag(
            id = getRandomId().toString(),
            name = getRandomTagName()
        )
    }

    fun getRandomGallery() = Gallery(
        id = getRandomId(),
        name = getRandomName(),
        description = getRandomDescription(),
        postList = getRandomPostList()
    )

    fun getRandomPost() = Post(
        id = getRandomId(),
        imageUrl = getRandomImageUrl(),
        description = getRandomDescription()
    )

    fun getRandomGalleryThumbnail() = GalleryThumbnail(
        galleryId = getRandomId(),
        imageUrl = getRandomImageUrl(),
        description = getRandomDescription()
    )

    fun getRandomGalleryPostContentModel() = PostContentModel(
        postId = getRandomName(),
        authorId = getRandomName(),
        imageUrl = getRandomImageUrl(),
        description = getRandomDescription(),
        registerAt = "2023-01-01",
        tags = listOf(getRandomTagName()),
        isFollower = true,
        reactions = listOf(Reactions.STAR),
        myReaction = Reactions.STAR
    )

    fun getRandomPostList() = getRandomList {
        getRandomPost()
    }

    fun getRandomGalleryList() = getRandomList(20, 20) {
        getRandomGallery()
    }


    fun getRandomGalleryThumbnailList() = getRandomList(20, 20) {
        getRandomGalleryThumbnail()
    }


    fun getTagByName(name: String): List<Tag> {
        val a = tagNameList.filter { name in it }.map {
            Tag(
                id = getRandomId().toString(),
                name = it
            )
        }
        Log.e("test", "source: $a")
        return a
    }

//    fun getFollowerAlbums() = followers

    private inline fun <reified T> getRandomList(
        min: Int = 1,
        max: Int = 20,
        noinline generator: (Int) -> T
    ): List<T> =
        Array((min..max).random(), generator).toCollection(mutableListOf())

}
