package com.porring.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage

@Composable
internal fun RandomImageList(
    padding: PaddingValues = PaddingValues()
) {
    val pagerState = rememberPagerState(pageCount = { 10 })

    HorizontalPager(
        state = pagerState
    ) { page ->
        ImageCard(page)
    }
}

@Composable
private fun ImageCard(
    idx: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 5f)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        RandomImage()
        Text(
            text = "idx: $idx"
        )
    }
}

@Composable
private fun RandomImage() {
    AsyncImage(
        model = "https://img.freepik.com/free-photo/three-smartphones-grey-background_125540-789.jpg?t=st=1730956720~exp=1730960320~hmac=cc7c5a7245b9de5e21ca5283a8e3c90ee80487bc968cdb8514ed0d6a32bb0bea&w=996",
        contentDescription = null
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewRandomImageList() {
    RandomImageList()
}