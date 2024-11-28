package com.kolown.home.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kolown.home.R
import com.kolown.model.Reactions

@Composable
internal fun LottieFireWorkAnimation(
    modifier: Modifier,
    reactions: List<Reactions>,
    myReaction: Reactions?
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fireworks))
    var isAnimationPlaying by remember { mutableStateOf(false) }
    var isFirstShow by remember { mutableStateOf(true) }

    LaunchedEffect(reactions) {
        if (reactions.isNotEmpty() && myReaction != null && !isFirstShow) {
            isAnimationPlaying = true
        }
        isFirstShow = false
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isAnimationPlaying,
        iterations = 1,
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            isAnimationPlaying = false
        }
    }

    if (isAnimationPlaying) {
        LottieAnimation(
            modifier = modifier.size(140.dp),
            composition = composition,
            progress = { progress }
        )
    }
}