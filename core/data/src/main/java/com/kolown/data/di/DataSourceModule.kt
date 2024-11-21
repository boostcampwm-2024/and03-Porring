package com.kolown.data.di

import com.kolown.data.datasource.AuthDataSource
import com.kolown.data.datasource.AuthDataSourceImpl
import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.datasource.remote.ImageDataSource
import com.kolown.data.datasource.remote.ImageDataSourceImpl
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.PostDataSourceImpl
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.ReactionDataSourceImpl
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.data.datasource.remote.TagDataSourceImpl
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
}
