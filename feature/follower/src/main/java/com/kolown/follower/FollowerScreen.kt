package com.kolown.follower

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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

    } else {
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFollowerScreen() {
    FollowerScreen(false)
}
