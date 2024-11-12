package com.kolown.camera

import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun PreviewViewCompose (cameraController: LifecycleCameraController){
    AndroidView(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectTapGestures {
            }
        },
        factory = { ctx ->
            // Initialize the PreviewView and configure it
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                controller = cameraController // Set the controller to manage the camera lifecycle
            }
        },
        onRelease = {
            // Release the camera controller when the composable is removed from the screen
            cameraController.unbind()
        }
    )
}
