package com.kolown.camera.screen

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kolown.camera.PreviewViewCompose
import com.kolown.camera.screen.component.CaptureButton
import com.kolown.camera.screen.component.FocusSurface
import com.kolown.camera.takePhoto
import java.util.concurrent.Executors

@Composable
fun CameraXCompose() {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            setImageAnalysisAnalyzer(cameraExecutor) { imageProxy ->
                imageProxy.close()
            }
            bindToLifecycle(lifecycle)
            isTapToFocusEnabled = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PreviewViewCompose(cameraController)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // 높이 지정
                .background(Color.Black.copy(alpha = 0.6f)) // 반투명한 색상
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                val nowSelector = cameraController.cameraSelector
                if (nowSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            }, modifier = Modifier.size(50.dp)) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    imageVector = Icons.Default.Face
                )

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // 높이 지정
                .background(Color.Black.copy(alpha = 0.6f)) // 반투명한 색상
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            CaptureButton {
                cameraController.takePhoto(context) {
                    // 사진 촬영 후 처리
                }
            }
        }
    }


}
