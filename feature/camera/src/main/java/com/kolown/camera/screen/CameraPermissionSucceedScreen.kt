package com.kolown.camera.screen

import android.net.Uri
import android.os.Build
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.camera.R
import com.kolown.camera.getImagePickerLauncher
import com.kolown.camera.screen.component.CaptureButton
import com.kolown.camera.screen.component.PreviewViewCompose
import com.kolown.camera.takePhoto
import java.io.IOException
import java.util.concurrent.Executors

@Composable
fun CameraXCompose(
    viewModel: CameraScreenViewModel,
    navigateToUpload: (String) -> Unit = {},
    popBackStack: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val imagePickerLauncher = getImagePickerLauncher()

    val uri = viewModel.uri.collectAsStateWithLifecycle()
    LaunchedEffect(uri.value) {
        Log.e("이미지 클릭3", uri.value.toString())
        uri.value?.let {
            navigateToUpload(it.toString())
        }
    }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            //어떤 카메라를 사용할 지 선택한다.
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            //이미지, 비디오 캡쳐를 위한 설정을 한다.(UseCase를 활성화 한다.)
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            //이미지 분석을 위한 설정을 한다.
            setImageAnalysisAnalyzer(cameraExecutor) { imageProxy ->
                imageProxy.close()
            }
            bindToLifecycle(lifecycle)

            //기타 세팅을 설정해 준다.
            isTapToFocusEnabled = true
            isPinchToZoomEnabled = true
        }
    }

    val cameraFlashExist = remember { cameraController.cameraInfo?.hasFlashUnit() == true }
    var cameraFlashState = remember { false }

    Box(modifier = Modifier.fillMaxSize()) {
        PreviewViewCompose(cameraController)
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .background(Color.Black.copy(alpha = 0.6f)),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = popBackStack, modifier = Modifier.size(48.dp)) {
                    Icon(
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "icon_back",
                        imageVector = ImageVector.vectorResource(R.drawable.icon_back_button_white)
                    )
                }

                IconButton(onClick = {
                    cameraFlashState = !cameraFlashState
                    Log.e("test","flash state: $cameraFlashState")
                        cameraController.enableTorch(cameraFlashState)
                }, modifier = Modifier.size(48.dp)) {
                    Icon(tint = Color.White,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "icon_flash",
                        imageVector = ImageVector.vectorResource(R.drawable.icon_flash)
                    )
                }
//                IconButton(onClick = {
//                    val nowSelector = cameraController.cameraSelector
//                    if (nowSelector == CameraSelector.DEFAULT_BACK_CAMERA)
//                        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//                    else
//                        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//                }, modifier = Modifier.size(50.dp)) {
//                    Icon(
//                        modifier = Modifier.fillMaxSize(),
//                        contentDescription = "",
//                        imageVector = Icons.Default.Face
//                    )
//
//                }
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
                    viewModel.saveBitmapToCache(it)
                }
            }
            IconButton(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd),

                onClick = {
                    imagePickerLauncher.launch { it ->
                        it?.let {
                            viewModel.setUri(it)
                        } ?: run {
                        }
                    }
                }) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    imageVector = ImageVector.vectorResource(R.drawable.icon_album)
                )
            }
        }

    }


}
