package com.kolown.data.di

import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.datasource.FakePostDataSource
import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.repository.FakeTagRepository
import com.kolown.data.repository.GalleryRepositoryImpl
import com.kolown.data.repository.GalleryRepository
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.PostRepositoryImpl
import com.kolown.data.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideFakePostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

}
