package com.kolown.camera

import android.media.Image
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


@Composable
fun getImagePickerLauncher(): ImagePickerLauncher {

    val launcher =  ImagePickerLauncher()
    launcher.ProvideLauncher(
        imagePickerProvider = { onSucceed ->
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = {
                onSucceed(it)
            }
        )
    }, photoPickerProvider = {  onSucceed ->
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                Log.d("이미지:result",it.toString())
                onSucceed(it)
            }
        )
    }
    )

    return launcher
}

class ImagePickerLauncher {

    private var onSucceed: (Uri?) -> Unit = {}
    private var imagePickerLauncher: ManagedActivityResultLauncher<String, Uri?>? = null
    private var photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? =
        null

    @Composable
    fun ProvideLauncher(
        imagePickerProvider: @Composable ((Uri?) -> Unit) -> ManagedActivityResultLauncher<String, Uri?>,
        photoPickerProvider: @Composable ((Uri?) -> Unit) -> ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        imagePickerLauncher = imagePickerProvider { uri ->
            onSucceed(uri)
        }
        photoPickerLauncher = photoPickerProvider { uri ->
            onSucceed(uri)
        }
        //imagePickerLauncher = imagePickerProvider(onSucceed)
    }


    fun launch(onFinished: (Uri?) -> Unit) {
        onSucceed = { uri ->
            onFinished(uri)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            photoPickerLauncher?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            imagePickerLauncher?.launch("image/*")
        }

    }

}
