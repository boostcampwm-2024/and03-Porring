package com.kolown.data.datasource

import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

interface AuthDataSource {
    suspend fun signInWithCredential(credential: CustomCredential): Result<Unit>
    fun getUserInfo(): UserDto
    fun checkUserLoggedIn(): Boolean
}

@Named("google")
class AuthDataSourceImpl @Inject constructor() : AuthDataSource {
    private val auth by lazy { Firebase.auth }

    override suspend fun signInWithCredential(credential: CustomCredential): Result<Unit> {
        return kotlin.runCatching {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null).let {
                auth.signInWithCredential(it).await()
            }
        }
    }

    override fun getUserInfo(): UserDto {
        val currentUser = auth.currentUser ?: throw Exception("로그인 안된 유저")

        return UserDto(userId = "user-${currentUser.uid}", email = currentUser.email.orEmpty())
    }

    override fun checkUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}

data class UserDto(
    val userId: String = "",
    val email: String = "",
)