package com.kolown.data.repository

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kolown.data.datasource.remote.AuthDataSource
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
    @Named("google") private val googleAuthDataSource: AuthDataSource,
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
        return googleAuthDataSource.signInWithEmailAndPassword(email, password)
    }

    override suspend fun joinWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return googleAuthDataSource.joinWithEmailAndPassword(email, password)
    }
}

