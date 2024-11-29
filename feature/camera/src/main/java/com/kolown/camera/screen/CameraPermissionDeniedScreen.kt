package com.kolown.camera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.ui.theme.BackgroundDark
import com.kolown.designsystem.ui.theme.PrimaryDark

@Composable
fun CameraPermissionDeniedScreen(
    navigateToSystemSettings: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(padding)
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text("카메라 권한이 거부되었습니다.", color = Color.White)
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = navigateToSystemSettings) {
            Text("설정으로 이동", color = PrimaryDark)
        }
    }
}
