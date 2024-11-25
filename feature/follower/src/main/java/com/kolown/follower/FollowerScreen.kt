package com.kolown.follower

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kolown.follower.component.FollowContent
import com.kolown.follower.component.RestrictedLoginContent

@Composable
internal fun FollowerRoute(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    padding: PaddingValues = PaddingValues(),
) {
    FollowerScreen(
        isLoggedIn = isLoggedIn,
        navigateToLogin = navigateToLogin,
        padding = padding
    )
}

@Composable
private fun FollowerScreen(
    isLoggedIn: Boolean = false,
    navigateToLogin: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    val modifier = Modifier
        .fillMaxSize()
        .padding(padding)

    if (isLoggedIn) {
        FollowContent()
    } else {
        RestrictedLoginContent(
            navigateToLogin = navigateToLogin,
        )
    }
}