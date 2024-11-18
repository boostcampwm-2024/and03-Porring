package com.kolown.data.di

import com.kolown.data.datasource.FakeGalleryDataSource
import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.repository.GalleryRepositoryImpl
import com.kolown.data.repository.GalleryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Fake
    @Provides
    @Singleton
    fun provideGalleryRepositoryFake(@Fake dataSource: GalleryDataSource): GalleryRepository {
        return GalleryRepositoryImpl(dataSource)
    }
}
