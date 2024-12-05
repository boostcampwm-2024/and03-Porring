package com.kolown.data.repository

import com.kolown.network.RemoteConfigDataSource
import javax.inject.Inject

interface RemoteConfigRepository {
    suspend fun getVersionName(): String?
}

class RemoteConfigRepositoryImpl @Inject constructor(private val remoteConfigDataSource: RemoteConfigDataSource) :
    RemoteConfigRepository {
    override suspend fun getVersionName(): String? =
        remoteConfigDataSource.getVersionName()

}
