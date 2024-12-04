package com.kolown.common.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import com.kolown.model.SnackBarData


val LocalSnackBarBridge = compositionLocalOf<SnackBarBridge> { error("No SnackBarHostState provided") }

suspend fun SnackbarHostState.showSnackBarWithData(data: SnackBarData) =
    showSnackbar(
        message = data.message,
        actionLabel = data.actionLabel,
        duration = SnackbarDuration.Short
    )

