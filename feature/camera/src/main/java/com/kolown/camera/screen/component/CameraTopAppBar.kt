package com.kolown.camera.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kolown.camera.R

@Composable
fun CameraTopAppBar(
    cameraPermission: Boolean,
    onChangeFlashState: () -> Unit,
    popBackStack: () -> Unit,
    padding: PaddingValues,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = padding.calculateTopPadding())
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = popBackStack, modifier = Modifier.size(48.dp)
        ) {
            Icon(
                tint = Color.White,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "icon_back",
                imageVector = ImageVector.vectorResource(R.drawable.icon_back_button_white)
            )
        }

        if (cameraPermission) {
            IconButton(
                onClick = onChangeFlashState, modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "icon_flash",
                    imageVector = ImageVector.vectorResource(R.drawable.icon_flash)
                )
            }
        }
    }
}