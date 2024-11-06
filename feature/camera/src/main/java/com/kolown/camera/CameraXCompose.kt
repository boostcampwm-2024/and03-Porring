package com.kolown.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Composable
fun CameraXCompose() {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
        }
    }
    var cameraViewImage = remember { Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    cameraController.setImageAnalysisAnalyzer(cameraExecutor) { imageProxy ->
        Log.e("test","analyze")
        cameraViewImage = CameraFilter.toGrayscale(imageProxy)
        imageProxy.close()
    }


    cameraController.bindToLifecycle(lifecycle)


    Image(
        bitmap = cameraViewImage.asImageBitmap(),
        contentDescription = "",
        modifier = Modifier.fillMaxSize()
    )

    //PreviewViewCompose(cameraController)

}
