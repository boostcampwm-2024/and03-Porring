package com.kolown.data.repository

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kolown.network.AuthDataSource
import com.kolown.datastore.LocalUserDataSource
import javax.inject.Inject
import javax.inject.Named

interface AuthRepository {
    suspend fun signInWithCredential(credential: Credential): Result<Unit>
    fun checkUserLoggedIn(): Boolean
    fun logout(): Result<Unit>
    suspend fun joinWithEmailAndPassword(email: String, password: String): Result<Unit>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit>
}

class AuthRepositoryImpl @Inject constructor(
    @Named("google") private val googleAuthDataSource: com.kolown.network.AuthDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : AuthRepository {
    override suspend fun signInWithCredential(credential: Credential): Result<Unit> {
        return kotlin.runCatching {
            when (credential) {
                is CustomCredential -> handleCustomCredential(credential)
            }
        }
    }

    private suspend fun handleCustomCredential(credential: CustomCredential) {
        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            googleAuthDataSource.signInWithCredential(credential)
        }
    }

    override fun checkUserLoggedIn(): Boolean {
        return googleAuthDataSource.checkUserLoggedIn()
    }

    override fun logout(): Result<Unit> {
        return googleAuthDataSource.logout()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return kotlin.runCatching {
            googleAuthDataSource.signInWithEmailAndPassword(email, password).getOrThrow()
                .let { userDto ->
                    localUserDataSource.createUserData(userDto)
                }
        }
    }

    override suspend fun joinWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return kotlin.runCatching {
            googleAuthDataSource.joinWithEmailAndPassword(email, password)
                .onSuccess { userDto ->
                    localUserDataSource.createUserData(userDto)
                    googleAuthDataSource.logout()
                }
                .onFailure { e -> throw e }
        }
    }
}

