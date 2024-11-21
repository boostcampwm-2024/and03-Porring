package com.kolown.data.di

import com.google.firebase.ktx.Firebase
import com.kolown.data.service.FirebaseService
import com.kolown.data.service.FirebaseServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseService(): FirebaseService {
        return FirebaseServiceImpl(Firebase)
    }
}