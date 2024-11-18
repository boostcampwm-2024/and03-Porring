package com.kolown.data.di

import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.repository.FakeGalleryRepository
import com.kolown.data.repository.GalleryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    @Fake
    fun provideGalleryRepository(@Fake dataSource: FakeGalleryDataSource): GalleryRepository {
        return FakeGalleryRepository(dataSource)
    }
}
