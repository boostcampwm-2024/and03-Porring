package com.kolown.follower

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

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
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {

        }
    } else {
        Column(
            modifier = modifier
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier = Modifier.size(120.dp),
                model = R.drawable.ic_porring_symbol,
                contentDescription = null
            )
            Spacer(Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.string_use_after_login),
                style = MaterialTheme.typography.labelLarge
            )
            TextButton(
                onClick = { navigateToLogin() },
            ) {
                Text(text = stringResource(R.string.string_move_to_login), color = Color.Blue)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFollowerScreen() {
    FollowerScreen(false)
}
