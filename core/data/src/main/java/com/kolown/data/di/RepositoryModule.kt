package com.kolown.data.di

import com.kolown.data.repository.AuthRepository
import com.kolown.data.repository.AuthRepositoryImpl
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.FollowRepositoryImpl
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.PostRepositoryImpl
import com.kolown.data.repository.TagRepository
import com.kolown.data.repository.TagRepositoryImpl
import com.kolown.data.repository.UserRepository
import com.kolown.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providePostRepository(
        postRepository: PostRepositoryImpl,
    ): PostRepository

    @Binds
    abstract fun provideAuthRepository(
        authRepository: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    abstract fun provideUserRepository(
        userRepository: UserRepositoryImpl,
    ): UserRepository

    @Binds
    abstract fun provideFollowRepository(
        followRepository: FollowRepositoryImpl,
    ): FollowRepository

    @Binds
    abstract fun provideFollowerRepository(
        followerRepository: FollowRepository,
    ): FollowRepository

    @Binds
    abstract fun provideTagRepository(
        tagRepository: TagRepositoryImpl,
    ): TagRepository

}
