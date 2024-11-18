package com.kolown.data.di

import com.kolown.data.datasource.GalleryDataSource
import com.kolown.data.datasource.FakeGalleryDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton





@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {
    @Fake
    @Provides
    @Singleton
    fun provideFakeGalleryDataSource(): GalleryDataSource {
        return FakeGalleryDataSource()
    }

}
