package com.kolown.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kolown.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.userDataStore by preferencesDataStore("user")
val LATEST_USER_EMAIL = stringPreferencesKey("latest_user_email")
val LATEST_USER_ID = stringPreferencesKey("latest_user_id")

class UserLocalDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {
    suspend fun createUserData(user: User): Result<Unit> {
        return kotlin.runCatching {
            applicationContext.userDataStore.edit { userDataStore ->
                userDataStore[LATEST_USER_EMAIL] = user.email
                userDataStore[LATEST_USER_ID] = user.userId
            }
        }
    }

    suspend fun getUserEmail(userId: String): Flow<String> {
        return applicationContext.userDataStore.data.map { it[LATEST_USER_EMAIL].orEmpty() }
    }
}
