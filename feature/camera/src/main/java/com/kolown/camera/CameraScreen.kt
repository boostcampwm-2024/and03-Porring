package com.kolown.camera

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat


@Composable
fun CameraScreen() {
    val context = LocalContext.current

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {

        }
        else {
        }
    }

    launcherMultiplePermissions.launch(permissions)

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Camera Screen")
        }
    }
}

private fun startCamera(context: Context) {
    //카메라 인스턴스 받아오기
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
            it.setAnalyzer(cameraExecutor, ImageAnalyzer())
        }

    //카메라 리스너 적용(future가 비동기이기 때문에 addListener를 사용해서 완료가 되면 카메라 띄움.)
    //continuation: -> coroutine
    cameraProviderFuture.addListener({

        // Used to bind the lifecycle of cameras to the lifecycle owner
        //지연 완료 메소드
        cameraProvider = cameraProviderFuture.get()


        // 어떤 카메라 사용할 것인지(back, front)
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind use cases before rebinding
            //바인드 된 항목들을 제거(surface, 카메라 종류 등 이전 다른 앱에서 카메라 모듈에 바인딩된 surface들을 제거)
            cameraProvider?.unbindAll()

            // Bind use cases to camera
            cameraProvider?.bindToLifecycle(
                this, cameraSelector,preview, imageAnalysis
            )

        } catch (exc: Exception) {

        }

    }, ContextCompat.getMainExecutor(requireContext()))
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen()
}


