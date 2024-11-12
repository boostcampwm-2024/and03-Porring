package com.kolown.camera.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun FocusSurface(content: @Composable () -> Unit) {
    var boxPosition by remember { mutableStateOf(IntOffset(0, 0)) }
    var isVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true){
                            val event = awaitPointerEvent()
                            val offset = event.changes.first().position
                            val x = (offset.x - 100).toInt()
                            val y = (offset.y - 100).toInt()
                            boxPosition = IntOffset(x, y)
                            isVisible = true
                        }
                    }
                },
        ) {


            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .offset { boxPosition }
                        .background(
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        .border(2.dp, Color.Yellow)
                )
            }
        }
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(1000) // 1초 후에 상자 사라짐
            isVisible = false
        }
    }
}
