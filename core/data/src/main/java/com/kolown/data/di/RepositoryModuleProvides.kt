package com.kolown.data.di

import android.content.Context
import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.repository.FakeTagRepository
import com.kolown.data.datasource.fake.FakeGalleryDataSource
import com.kolown.data.repository.GalleryRepository
import com.kolown.data.repository.GalleryRepositoryImpl
import com.kolown.data.repository.ImageCacheRepository
import com.kolown.data.repository.ImageCacheRepositoryImpl
import com.kolown.data.repository.TagRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleProvides {

    @Fake
    @Provides
    @Singleton
    fun provideGalleryRepositoryFake(): GalleryRepository {
        return GalleryRepositoryImpl(FakeGalleryDataSource())
    }
}
