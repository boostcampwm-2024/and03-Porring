package com.kolown.common.component

import com.kolown.model.SnackBarEvent

class SnackBarBridge(
    private val onSnackBarDataAdded: (SnackBarEvent) -> Unit
) {
    fun postSnackBarEvent(event: SnackBarEvent) {
        onSnackBarDataAdded(event)
    }

    fun postSnackBarString(message: String) {
        onSnackBarDataAdded(SnackBarEvent.Message(message, null))
    }
}
