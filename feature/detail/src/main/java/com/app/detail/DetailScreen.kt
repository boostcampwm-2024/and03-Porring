package com.porring.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.app.detail.R

@Composable
internal fun DetailRoute(
    padding: PaddingValues = PaddingValues(),
) {
    DetailScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "뒤로 가기",
                        tint = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        DetailContent(
            padding = padding,
            imageUrl = "https://img.freepik.com/free-photo/symmetrical-clouds-covered-blue-sky_198523-5.jpg",
            imageDescription = "퇴근하고 집가는 풍경 좋다"
        )
    }
}


@Composable
fun DetailContent(
    padding: PaddingValues,
    imageUrl : String = "https://img.freepik.com/free-photo/symmetrical-clouds-covered-blue-sky_198523-5.jpg",
    imageDescription: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        AsyncImage(
            model =  ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            onState = { state ->
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        Log.d("이미지  로드","Image Load Success")
                    }

                    is AsyncImagePainter.State.Error -> {
                        Log.d("이미지  로드","${state.result.throwable.message}")
                    }

                    else -> {}
                }
            },
        )
        Text(
            text = imageDescription,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 10.dp),
            color = Color.White,
            fontSize = 16.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                tint = Color(0xFF00BBFF),
                contentDescription = ""
            )
            Row {
                DetailButton(id = R.drawable.ic_detail_gallary, buttonText = "갤러리")
                Spacer(modifier = Modifier.width(10.dp))
                DetailButton(id = R.drawable.ic_detail_follow, buttonText = "팔로우")
            }
        }

    }
}


@Composable
fun DetailButton(
    @DrawableRes id: Int,
    buttonText: String
) {
    Button(
        onClick = {},
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D37))
    ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = null,
            tint = Color(0xFF00BBFF)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = buttonText, color = Color(0xFF00BBFF))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DetailScreenPreview() {
    DetailContent(
        padding = PaddingValues(0.dp),
        imageUrl = "https://www.adobe.com/content/dam/cc/us/en/creative-cloud/photography/discover/landscape-photography/CODERED_B1_landscape_P2d_714x348.jpg.img.jpg",
        imageDescription = "집으로 가는 길 풍경 좋다"
    )
}