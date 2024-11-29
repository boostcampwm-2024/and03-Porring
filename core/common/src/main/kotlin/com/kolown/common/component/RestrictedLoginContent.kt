package com.kolown.common.component

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kolown.common.R
import com.kolown.designsystem.ui.theme.Primary

@Composable
fun RestrictedLoginContent(
    navigateToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_porring_symbol),
            modifier = Modifier.size(120.dp),
            contentDescription = null,
            tint = Primary
        )

        Text(
            text = stringResource(R.string.string_use_after_login),
            style = MaterialTheme.typography.labelLarge
        )
        TextButton(
            onClick = { navigateToLogin() },
        ) {
            Text(
                text = stringResource(R.string.string_move_to_login),
                color = Primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRestrictedLoginContent() {
    RestrictedLoginContent()
}