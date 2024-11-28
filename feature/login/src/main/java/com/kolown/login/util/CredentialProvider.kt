package com.kolown.login.util

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.kolown.login.BuildConfig

internal suspend fun getCredential(
    platform: LoginPlatform,
    context: Context,
): Result<GetCredentialResponse> {
    return runCatching {
        val credentialManager = CredentialManager.create(context)
        val credentialOption = when (platform) {
            LoginPlatform.Google -> buildGoogleOptions()
        }
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption)
            .build()

        credentialManager.getCredential(
            request = request,
            context = context as Activity
        )
    }
}

private fun buildGoogleOptions(): GetGoogleIdOption {
    return GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .build()
}