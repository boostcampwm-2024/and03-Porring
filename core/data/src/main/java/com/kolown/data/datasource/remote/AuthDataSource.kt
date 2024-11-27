package com.kolown.data.datasource.remote

import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kolown.data.remote.UserDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

interface AuthDataSource {
    suspend fun signInWithCredential(credential: CustomCredential): Result<Unit>
    fun getUserId(): String
    fun getUserInfo(): UserDto
    fun checkUserLoggedIn(): Boolean
    fun logout(): Result<Unit>
    suspend fun joinWithEmailAndPassword(email: String, password: String): Result<Unit>
}

@Named("google")
class AuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthDataSource {
    override suspend fun signInWithCredential(credential: CustomCredential): Result<Unit> {
        return kotlin.runCatching {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null).let {
                auth.signInWithCredential(it).await()
            }
        }
    }

    override fun getUserId(): String {
        val currentUser = auth.currentUser ?: return ""

        return "user-${currentUser.uid}"
    }

    override fun getUserInfo(): UserDto {
        val currentUser = auth.currentUser ?: throw Exception("로그인 안된 유저")

        return UserDto(userId = "user-${currentUser.uid}", email = currentUser.email.orEmpty())
    }

    override fun checkUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun logout(): Result<Unit> {
        return kotlin.runCatching {
            auth.signOut()
        }
    }

    override suspend fun joinWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return kotlin.runCatching {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.signOut()
        }
    }
}