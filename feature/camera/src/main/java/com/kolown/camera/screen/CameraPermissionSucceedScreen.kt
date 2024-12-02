package com.kolown.camera.screen

import android.content.Context
import android.media.AudioManager
import android.media.MediaActionSound
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraState
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kolown.camera.R
import com.kolown.camera.getImagePickerLauncher
import com.kolown.camera.getSuspendedResult
import com.kolown.camera.screen.component.CaptureButton
import com.kolown.camera.screen.component.GridLineCompose
import com.kolown.camera.screen.component.PreviewViewCompose
import com.kolown.camera.takePhoto
import kotlinx.coroutines.launch
import com.kolown.designsystem.ui.theme.BackgroundDark

@Composable
fun CameraPermissionSucceedScreen(
    isFlashOn: Boolean = false,
    viewModel: CameraScreenViewModel = hiltViewModel(),
    navigateToUpload: (String) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val imagePickerLauncher = getImagePickerLauncher()
    var cameraCaptureState = remember { true }


    val uri by viewModel.uri.collectAsStateWithLifecycle()

    LaunchedEffect(uri) {
        uri?.let { navigateToUpload(it.toString()) }
    }


    val cameraController = remember {
        LifecycleCameraController(context).apply {
            //어떤 카메라를 사용할 지 선택한다.
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            //이미지, 비디오 캡쳐를 위한 설정을 한다.(UseCase를 활성화 한다.)
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            bindToLifecycle(lifecycle)

            //기타 세팅을 설정해 준다.
            isTapToFocusEnabled = true
            isPinchToZoomEnabled = true

            previewResolutionSelector = ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                .build()

        }
    }

    var cameraStateLiveData: LiveData<CameraState>? = null

    //안전한 사용을 위한 카메라 state 변수
    var cameraState: CameraState? by remember { mutableStateOf(null) }

    //카메라의 상태를 LiveData로 주기 때문에 그에 대한 대응으로
    val observer = remember {
        Observer<CameraState> { state ->
            cameraState = state
        }
    }


    LaunchedEffect(Unit) {
        //카메라가 완료될 때 까지 대기
        cameraController.initializationFuture.getSuspendedResult(context)
        //livedata 저장
        cameraStateLiveData = cameraController.cameraInfo?.cameraState
        //observing
        cameraStateLiveData?.observeForever(observer)
    }

    DisposableEffect(Unit) {
        //dispose 될 시 해제
        onDispose {
            cameraStateLiveData?.removeObserver(observer)
        }
    }

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val onShutterClick = {
        lifecycle.lifecycleScope.launch {
            //MediaActionSound.mustPlayShutterSound()
            MediaActionSound().play(MediaActionSound.SHUTTER_CLICK)
        }
    }

    LaunchedEffect(isFlashOn) {
        cameraController.enableTorch(isFlashOn)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = padding.calculateBottomPadding())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black),
        )

//        GridLineCompose(
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(3f / 4f)
//        )

        PreviewViewCompose(
            cameraController,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            CaptureButton {
                onShutterClick()
                cameraController.takePhoto(context) {
                    //카메라가 완전히 OPEN 되어 있을 때만(모영민님 피드백)
                    if (cameraState?.type == CameraState.Type.OPEN && cameraCaptureState) {
                        cameraCaptureState = false
                        cameraController.takePhoto(context) {
                            cameraCaptureState = true
                            viewModel.saveBitmapToCache(it)
                        }
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
                            cameraController.cameraSelector =
                                CameraSelector.DEFAULT_FRONT_CAMERA
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
