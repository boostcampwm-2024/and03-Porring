package com.kolown.camera.screen

import android.content.Context
import android.media.AudioManager
import android.media.MediaActionSound
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kolown.camera.R
import com.kolown.camera.getImagePickerLauncher
import com.kolown.camera.screen.component.CaptureButton
import com.kolown.camera.screen.component.GridLineCompose
import com.kolown.camera.screen.component.PreviewViewCompose
import com.kolown.camera.takePhoto
import com.kolown.designsystem.ui.theme.BackgroundDark
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Composable
fun CameraXCompose(
    isFlashOn: Boolean = false,
    viewModel: CameraScreenViewModel = hiltViewModel(),
    navigateToUpload: (String) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val imagePickerLauncher = getImagePickerLauncher()
    val uri = viewModel.uri.collectAsStateWithLifecycle()
    LaunchedEffect(uri.value) {
        uri.value?.let { navigateToUpload(it.toString()) }
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
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val onShutterClick = {
        lifecycle.lifecycleScope.launch {
            MediaActionSound.mustPlayShutterSound()
            MediaActionSound().play(MediaActionSound.SHUTTER_CLICK)
        }
    }

    LaunchedEffect(isFlashOn) {
        cameraController.enableTorch(isFlashOn)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PreviewViewCompose(cameraController)

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(BackgroundDark.copy(alpha = 0.8f)),
            )

            GridLineCompose(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4 / 5f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp) // 높이 지정
                    .background(BackgroundDark.copy(alpha = 0.8f)) // 반투명한 색상
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                CaptureButton {
                    onShutterClick()
                    cameraController.takePhoto(context) {
                        viewModel.saveBitmapToCache(it)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(BackgroundDark),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    modifier = Modifier
                        .size(48.dp),

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
                        contentDescription = "icon_album",
                        imageVector = ImageVector.vectorResource(R.drawable.icon_album_white),
                        tint = Color.White
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(48.dp),
                    onClick = {
                        val nowSelector = cameraController.cameraSelector
                        if (nowSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                        else
                            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    }) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "icon_switch_camera",
                        imageVector = ImageVector.vectorResource(R.drawable.icon_switch_camera_white),
                        tint = Color.White
                    )
                }
            }
        }
    }
}