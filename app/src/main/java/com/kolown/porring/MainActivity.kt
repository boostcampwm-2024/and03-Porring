package com.kolown.porring

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kolown.porring.ui.theme.PorringTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            PorringTheme {
                ImagePicker()
//                MainScreen(
//                    navigator = navigator
//                )
            }
        }
    }

    fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val file = File(applicationContext.cacheDir, "photo_${System.currentTimeMillis()}.jpg")

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)   // todo 상수 변환 필요
        }

        return Uri.fromFile(file)
    }

    fun clearCacheFiles() {
        val cacheDir = applicationContext.cacheDir
        if (cacheDir.isDirectory) {
            cacheDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }

    fun decodeSampledBitmapFromUri(
        context: Context,
        uri: Uri,
    ): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options)

        options.inJustDecodeBounds = false
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        // todo 상수 변환 필요(900, 720)

        if (height > 900 || width > 720) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= 900 && halfWidth / inSampleSize >= 720) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}

// 편집 화면에서 사용할 Resizing 된 이미지
// 필요시 수정해주세요
@Composable
fun ResizedImage(
    context: Context,
    uri: Uri,
    decodeSampledBitmapFromResource: (Context, Uri) -> Bitmap?,
) {
    // 이 bitmap을 편집하고 업로드할 때 이 bitmap을 업로드 하면 될듯
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uri) {
        bitmap = withContext(Dispatchers.IO) {
            decodeSampledBitmapFromResource(context, uri)
        }
    }

    bitmap?.let {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f),  // todo 상수로 변환 필요
            model = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ImagePicker() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(100.dp))
        Button(onClick = {

        }) {
            Text(text = "Pick Image")
        }

        imageUri?.let {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f),  // todo 상수로 변환 필요
                model = it,
                contentDescription = null,
            )
        }
    }
}
