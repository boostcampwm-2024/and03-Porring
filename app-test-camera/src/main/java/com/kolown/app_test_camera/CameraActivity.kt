package com.kolown.app_test_camera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.app_test_camera.ui.theme.PorringTheme
import com.kolown.camera.screen.CameraScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cameraPermissionDinedCount by
            viewModel.cameraPermissionDinedStateFlow.collectAsStateWithLifecycle(false, this)
            PorringTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        CameraScreen(cameraPermissionDinedProvider = { cameraPermissionDinedCount },
                            onCameraPermissionDined = {
                                viewModel.increaseCameraPermissionDinedCount()
                            },
                            onCameraPermissionGranted = {
                                viewModel.resetCameraPermissionDinedCount()
                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PorringTheme {
        Greeting("Android")
    }
}
