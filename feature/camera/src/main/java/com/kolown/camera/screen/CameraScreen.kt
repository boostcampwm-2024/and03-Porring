package com.kolown.camera.screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.kolown.camera.camera.permissions
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.kolown.camera.PermissionChecker


@Composable
internal fun CameraRoute(
    navigateToUpload: (String) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    popBackStack: () -> Unit = {}
) {
    CameraScreen(
        padding = padding,
        navigateToUpload = navigateToUpload,
        popBackStack = popBackStack
    )
}

@Composable
fun CameraScreen(
    padding: PaddingValues,
    navigateToUpload: (String) -> Unit = {},
    viewModel: CameraScreenViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalView.current.context as android.app.Activity
    var cameraPermission by remember {
        mutableStateOf(
            PermissionChecker.checkCameraPermission(
                context
            )
        )
    }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { areGranted ->
        Log.e("카메라", areGranted.toString())

        cameraPermission = areGranted
    }

    LaunchedEffect(cameraPermission) {
        launcherMultiplePermissions.launch(Manifest.permission.CAMERA)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        if (cameraPermission) {
            CameraXCompose(viewModel, navigateToUpload, popBackStack)
        } else {
            CameraPermissionDeniedScreen(
                popBackStack
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CameraScreenPreview() {
//    CameraScreen()
//}


