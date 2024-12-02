package com.kolown.camera.screen.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GridLineCompose(divideNum: Int = 3, modifier: Modifier = Modifier, strokeWidth: Dp = 1.dp) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val strokeWidthPx = strokeWidth.toPx()

        val width = size.width
        val height = size.height

        repeat(divideNum + 1) {
            drawLine(
                color = Color.White,
                start = Offset(0f, height / (divideNum) * it),
                end = Offset(width, height / (divideNum) * it),
                strokeWidth = strokeWidthPx
            )
        }

        repeat(divideNum + 1) {
            drawLine(
                color = Color.White,
                start = Offset(width / (divideNum) * it, 0f),
                end = Offset(width / (divideNum) * it, height),
                strokeWidth = strokeWidthPx
            )
        }

//        // 세로 줄
//        drawLine(
//            color = Color.White,
//            start = Offset(width / 3, 0f),
//            end = Offset(width / 3, height),
//            strokeWidth = strokeWidthPx
//        )
//        drawLine(
//            color = Color.White,
//            start = Offset(2 * width / 3, 0f),
//            end = Offset(2 * width / 3, height),
//            strokeWidth = strokeWidthPx
//        )


    }
}

