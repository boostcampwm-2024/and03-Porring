package com.kolown.data.di

import com.kolown.data.repository.GalleryRepository
import com.kolown.data.repository.GalleryRepositoryImpl
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideGalleryRepositoryFake(
        galleryRepositoryImpl: GalleryRepositoryImpl
    ): GalleryRepository

    @Binds
    abstract fun providePostRepository(
        postRepository: PostRepositoryImpl
    ): PostRepository
}
