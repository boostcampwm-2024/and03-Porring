package com.kolown.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraManager (context:Context) {

    private val cameraProviderFuture= ProcessCameraProvider.getInstance(context)

    private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(this).also { cameraProvider ->

                cameraProvider.addListener({
                    cameraProvider.get()
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(this))
            }
        }
    private fun startCamera() {
        //카메라 인스턴스 받아오기

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

            // Preview 세팅.
            // 먼저 빌드를 하고 surfaceProvider를 prevView걸로 제공
            val preview= Preview.Builder().build().apply {
                surfaceProvider = binding.surfaceView.surfaceProvider
            }


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

}
