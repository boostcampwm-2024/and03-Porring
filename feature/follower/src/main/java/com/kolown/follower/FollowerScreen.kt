package com.kolown.follower

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            modifier = modifier
        )
    }
}