package com.kolown.camera

import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun GLSurfaceViewCompose() {
    val context = LocalContext.current
    AndroidView(
        factory = { GLSurfaceView(context) },
        modifier = Modifier.fillMaxSize()
    )
}
