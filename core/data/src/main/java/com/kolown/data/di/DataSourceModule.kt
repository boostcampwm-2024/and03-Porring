package com.kolown.data.di

import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.datasource.ImageDataSource
import com.kolown.data.datasource.ImageDataSourceImpl
import com.kolown.data.datasource.PostDataSource
import com.kolown.data.datasource.PostDataSourceImpl
import com.kolown.data.datasource.TagDataSourceImpl
import com.kolown.data.datasource.TagDatasource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {
    @Fake
    @Singleton
    @Binds
    abstract fun provideFakeGalleryDataSource(
        galleryDataSource: FakeGalleryDataSource
    ): GalleryDataSource

    @Singleton
    @Binds
    abstract fun provideImageDataSource(
        imageDataSource: ImageDataSourceImpl
    ): ImageDataSource

    @Singleton
    @Binds
    abstract fun providePostDataSource(
        postDataSource: PostDataSourceImpl
    ): PostDataSource

    @Singleton
    @Binds
    abstract fun provideTagDataSource(
        tagDataSource: TagDataSourceImpl
    ): TagDatasource
}
