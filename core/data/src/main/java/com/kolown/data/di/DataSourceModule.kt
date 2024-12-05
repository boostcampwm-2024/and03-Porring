package com.kolown.data.di

import com.kolown.data.datasource.fake.FakeGalleryDataSource
import com.kolown.data.datasource.fake.GalleryDataSource
import com.kolown.network.AuthDataSource
import com.kolown.network.AuthDataSourceImpl
import com.kolown.network.FollowDataSource
import com.kolown.network.FollowDataSourceImpl
import com.kolown.network.ImageDataSource
import com.kolown.network.ImageDataSourceImpl
import com.kolown.network.PostDataSource
import com.kolown.network.PostDataSourceImpl
import com.kolown.network.ReactionDataSource
import com.kolown.network.ReactionDataSourceImpl
import com.kolown.network.TagDataSource
import com.kolown.network.TagDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Fake
    @Singleton
    @Binds
    abstract fun bindFakeGalleryDataSource(
        galleryDataSource: FakeGalleryDataSource,
    ): GalleryDataSource

    @Binds
    abstract fun bindImageDataSource(
        imageDataSource: com.kolown.network.ImageDataSourceImpl,
    ): com.kolown.network.ImageDataSource

    @Binds
    abstract fun bindPostDataSource(
        postDataSource: com.kolown.network.PostDataSourceImpl,
    ): com.kolown.network.PostDataSource

    @Binds
    abstract fun bindTagDataSource(
        tagDataSource: com.kolown.network.TagDataSourceImpl,
    ): com.kolown.network.TagDataSource

    @Binds
    abstract fun bindReactionDataSource(
        reactionDataSource: com.kolown.network.ReactionDataSourceImpl,
    ): com.kolown.network.ReactionDataSource

    @Named("google")
    @Binds
    abstract fun bindsAuthDatsSource(
        authDataSource: com.kolown.network.AuthDataSourceImpl,
    ): com.kolown.network.AuthDataSource


    @Binds
    abstract fun bindUploadDataSource(
        followDataSource: com.kolown.network.FollowDataSourceImpl,
    ): com.kolown.network.FollowDataSource

}
