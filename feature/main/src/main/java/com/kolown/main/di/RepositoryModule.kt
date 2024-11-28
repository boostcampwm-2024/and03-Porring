package com.kolown.main.di

import android.content.Context
import com.kolown.data.repository.FakeImageRepository
import com.kolown.data.repository.FakeImageRepositoryImpl
import com.kolown.data.repository.ImageCacheRepository
import com.kolown.data.repository.ImageCacheRepositoryImpl
import com.kolown.data.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ImageCacheModule {
    @Provides
    @Singleton
    fun provideImageCacheRepository(@ApplicationContext applicationContext: Context): ImageCacheRepository {
        return ImageCacheRepositoryImpl(applicationContext)
    }
}


@Module
@InstallIn(SingletonComponent::class)
class ImageModule {
    @FakeImageRepository
    @Provides
    @Singleton
    fun provideFakeImageRepository(): ImageRepository {
        return FakeImageRepositoryImpl()
    }
}

