package com.kolown.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraXCompose() {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var cameraViewImage = remember { Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888) }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            setImageAnalysisAnalyzer(cameraExecutor) { imageProxy ->
                Log.e("test","imageAnalyze")
                cameraViewImage = CpuFilter.toGrayscale(imageProxy)
                imageProxy.close()
            }
            bindToLifecycle(lifecycle)

        }
    }





    Image(
        bitmap = cameraViewImage.asImageBitmap(),
        contentDescription = "",
        modifier = Modifier.fillMaxSize()
    )

    //PreviewViewCompose(cameraController)

}
