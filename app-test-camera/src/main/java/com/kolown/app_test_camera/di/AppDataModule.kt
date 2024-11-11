package com.kolown.app_test_camera.di

import android.content.Context
import com.kolown.data.datasource.AppDataSource
import com.kolown.data.datasource.AppDataSourceImpl
import com.kolown.data.repository.AppDataRepository
import com.kolown.data.repository.AppDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppDataModule {

    @Provides
    @Singleton
    fun provideAppData(appDataSource: AppDataSource): AppDataRepository {
        return AppDataRepositoryImpl(appDataSource)
    }

    @Provides
    @Singleton
    fun providesAppDataSource(@ApplicationContext context: Context): AppDataSource {
        return AppDataSourceImpl(context)
    }
}
