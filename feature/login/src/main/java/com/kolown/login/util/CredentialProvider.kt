package com.kolown.login.util

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.kolown.login.BuildConfig

suspend fun getCredential(platform: LoginPlatform, context: Context): Result<Credential> {
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
            context = context
        ).credential
    }
}

private fun buildGoogleOptions(): GetGoogleIdOption {
    return GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .build()
}