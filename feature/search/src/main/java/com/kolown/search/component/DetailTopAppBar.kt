package com.kolown.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kolown.search.R

@Composable
internal fun DetailTopAppBar(
    isReelsMode: Boolean,
    onChangeReelsMode: (Boolean) -> Unit,
    popBackStack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isReelsMode) {
            Arrangement.spacedBy(8.dp, Alignment.Start)
        } else {
            Arrangement.spacedBy(8.dp, Alignment.End)
        }
    ) {
        if (isReelsMode) {
            IconButton(
                onClick = { popBackStack() }, modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_back),
                    contentDescription = stringResource(R.string.string_go_back_button),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            IconButton(
                onClick = { onChangeReelsMode(true) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.string_disable_mode),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}