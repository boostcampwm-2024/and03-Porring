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


@Composable
fun getImagePickerLauncher(): ImagePickerLauncher {

    val launcher = ImagePickerLauncher()
    launcher.ProvideLauncher({
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(), it
        )
    }, {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(), it
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
        Log.d("이미지:provide", "Selected image URI: $")
        imagePickerLauncher = imagePickerProvider(onSucceed)
        photoPickerLauncher = photoPickerProvider(onSucceed)

    }


    fun launch(onFinished: (Uri?) -> Unit) {
        onSucceed = { uri ->
            // URI를 로그로 출력
            Log.d("이미지:launch", "Selected image URI: $uri")
            onFinished(uri)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            photoPickerLauncher?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            imagePickerLauncher?.launch("image/*")
        }

    }

}
