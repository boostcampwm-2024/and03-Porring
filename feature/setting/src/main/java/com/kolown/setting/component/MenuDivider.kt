package com.kolown.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.ui.theme.Surface

@Composable
internal fun MenuDivider() {
    Box(
        modifier = Modifier.fillMaxWidth().height(8.dp).background(Surface),
    )
}