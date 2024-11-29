package com.kolown.camera.screen

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import com.kolown.camera.PermissionChecker
import com.kolown.camera.screen.component.CameraTopAppBar


@Composable
internal fun CameraRoute(
    navigateToUpload: (String) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    popBackStack: () -> Unit = {},
    viewModel: CameraScreenViewModel = hiltViewModel(),
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

    val navigateToSystemSettings = {
        val intent =
            android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .apply {
                    data = android.net.Uri.fromParts("package", context.packageName, null)
                }
        context.startActivity(intent)
    }

    CameraScreen(
        cameraPermission = cameraPermission,
        navigateToSystemSettings = navigateToSystemSettings,
        navigateToUpload = navigateToUpload,
        popBackStack = popBackStack,
        padding = padding
    )
}

@Composable
private fun CameraScreen(
    cameraPermission: Boolean = false,
    navigateToSystemSettings: () -> Unit = {},
    navigateToUpload: (String) -> Unit = {},
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    var cameraFlashState by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (cameraPermission) {
            CameraXCompose(
                isFlashOn = cameraFlashState,
                navigateToUpload = navigateToUpload,
                padding = padding
            )
        } else {
            CameraPermissionDeniedScreen(
                navigateToSystemSettings = navigateToSystemSettings,
                padding = padding
            )
        }

        CameraTopAppBar(
            cameraPermission = cameraPermission,
            onChangeFlashState = { cameraFlashState = !cameraFlashState },
            popBackStack = popBackStack,
            padding = padding
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CameraScreenPreview() {
//    CameraScreen()
//}


