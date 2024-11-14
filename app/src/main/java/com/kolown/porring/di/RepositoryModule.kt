package com.kolown.porring.di

import android.content.Context
import com.kolown.data.repository.ImageCacheRepository
import com.kolown.data.repository.ImageCacheRepositoryImpl
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
