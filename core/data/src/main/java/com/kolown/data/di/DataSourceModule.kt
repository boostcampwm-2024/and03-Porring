package com.kolown.data.di

import com.kolown.data.datasource.fake.FakeGalleryDataSource
import com.kolown.data.datasource.fake.GalleryDataSource
import com.kolown.data.datasource.local.UserLocalDataSourceImpl
import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.AuthDataSourceImpl
import com.kolown.data.datasource.remote.FollowDataSource
import com.kolown.data.datasource.remote.FollowDataSourceImpl
import com.kolown.data.datasource.remote.ImageDataSource
import com.kolown.data.datasource.remote.ImageDataSourceImpl
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.PostDataSourceImpl
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.ReactionDataSourceImpl
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.data.datasource.remote.TagDataSourceImpl
import com.kolown.data.datasource.remote.UserDataSource
import com.kolown.data.datasource.remote.UserDataSourceImpl
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
    abstract fun provideFakeGalleryDataSource(
        galleryDataSource: FakeGalleryDataSource,
    ): GalleryDataSource

    @Binds
    abstract fun provideImageDataSource(
        imageDataSource: ImageDataSourceImpl,
    ): ImageDataSource

    @Binds
    abstract fun providePostDataSource(
        postDataSource: PostDataSourceImpl,
    ): PostDataSource

    @Binds
    abstract fun provideTagDataSource(
        tagDataSource: TagDataSourceImpl,
    ): TagDataSource

    @Binds
    abstract fun provideReactionDataSource(
        reactionDataSource: ReactionDataSourceImpl,
    ): ReactionDataSource

    @Named("google")
    @Binds
    abstract fun provideAuthDatsSource(
        authDataSource: AuthDataSourceImpl,
    ): AuthDataSource

    @Named("google")
    @Binds
    abstract fun provideUserDataSource(
        userDataSource: UserDataSourceImpl,
    ): UserDataSource

    @Named("LDS")
    @Binds
    abstract fun provideUserLocalDataSource(
        userDataSource: UserLocalDataSourceImpl,
    ): UserDataSource

    @Binds
    abstract fun provideUploadDataSource(
        followDataSource: FollowDataSourceImpl,
    ): FollowDataSource

}
