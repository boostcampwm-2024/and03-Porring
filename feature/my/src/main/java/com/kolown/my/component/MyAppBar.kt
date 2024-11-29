package com.kolown.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun MyAppBar(onSettingClicked: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White)
    ) {
        Text(
            "My Gallery",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onSettingClicked, modifier = Modifier.align(Alignment.CenterEnd)) {
            Icon(Icons.Filled.Settings, contentDescription = null)
        }

    }
}

@Composable
@Preview
private fun MyAppBarPreview() {
    MyAppBar()
}
