package com.kolown.camera.screen.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GridLineCompose(modifier: Modifier = Modifier, strokeWidth: Dp = 1.dp) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidthPx = strokeWidth.toPx()

        val width = size.width
        val height = size.height

        drawLine(
            color = Color.White,
            start = Offset(0f, 0f),
            end = Offset(width, 0f)
        )
        // 세로 줄
        drawLine(
            color = Color.White,
            start = Offset(width / 3, 0f),
            end = Offset(width / 3, height),
            strokeWidth = strokeWidthPx
        )
        drawLine(
            color = Color.White,
            start = Offset(2 * width / 3, 0f),
            end = Offset(2 * width / 3, height),
            strokeWidth = strokeWidthPx
        )

        // 가로 줄
        drawLine(
            color = Color.White,
            start = Offset(0f, height / 3),
            end = Offset(width, height / 3),
            strokeWidth = strokeWidthPx
        )
        drawLine(
            color = Color.White,
            start = Offset(0f, 2 * height / 3),
            end = Offset(width, 2 * height / 3),
            strokeWidth = strokeWidthPx
        )

        drawLine(
            color = Color.White,
            start = Offset(0f, height),
            end = Offset(width, height)
        )
    }
}

