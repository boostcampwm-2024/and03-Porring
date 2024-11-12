package com.kolown.camera.screen

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
import com.kolown.camera.PermissionChecker
import com.kolown.camera.screen.component.FocusSurface


@Composable
fun CameraScreen(
    cameraPermissionDinedProvider: () -> Boolean,
    onCameraPermissionDined: () -> Unit = {},
    onCameraPermissionGranted: () -> Unit = {},
) {
    val context = LocalContext.current
    val cameraPermissionDinedCompletely = cameraPermissionDinedProvider()
    var cameraPermission by remember {
        mutableStateOf(
            PermissionChecker.checkCameraPermission(
                context
            )
        )
    }

    Log.e("testing","count: $cameraPermissionDinedCompletely")

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->

        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            onCameraPermissionGranted()
            cameraPermission = true
        } else {
            onCameraPermissionDined()
        }
    }

    if (cameraPermission) {
        CameraXCompose()
    } else {
        CameraPermissionDeniedScreen()
        if (!cameraPermissionDinedCompletely) {
            SideEffect {
                launcherMultiplePermissions.launch(permissions)
            }
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }

    }


//    Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->
//        Column(modifier = Modifier.padding(innerPadding)) {
//
//       }
//    }

}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen({ false })
}


