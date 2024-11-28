package com.kolown.setting.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kolown.setting.R

@Composable
internal fun SettingTopAppBar(
    popBackStack: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
    ) {
        IconButton(
            onClick = { popBackStack() }, modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.string_back_button),
                modifier = Modifier.size(24.dp)
            )
        }

        Text(style = MaterialTheme.typography.titleLarge, text = "설정")
    }
}