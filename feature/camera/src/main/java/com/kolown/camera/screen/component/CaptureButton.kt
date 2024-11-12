package com.kolown.camera.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kolown.camera.R

@Composable
fun CaptureButton( modifier: Modifier = Modifier.size(62.dp),onClick: () -> Unit,) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_capture),
            contentDescription = "Capture",
            modifier = Modifier.fillMaxSize(),
            tint = Color.White
        )
    }

}
