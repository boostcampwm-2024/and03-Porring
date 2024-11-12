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
import androidx.compose.ui.platform.LocalView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.kolown.camera.PermissionChecker
import com.kolown.camera.screen.component.FocusSurface


@Composable
fun CameraScreen(
    cameraPermissionDinedProvider: () -> Boolean,
    onCameraPermissionDined: () -> Unit = {},
    onCameraPermissionGranted: () -> Unit = {},
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
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->

        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            onCameraPermissionGranted()
            cameraPermission = true
        } else {
            if(!shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
            onCameraPermissionDined()
        }
    }
    SideEffect {
        launcherMultiplePermissions.launch(permissions)
    }

    if (cameraPermission) {
        CameraXCompose()
    } else {
        CameraPermissionDeniedScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen({ false })
}


