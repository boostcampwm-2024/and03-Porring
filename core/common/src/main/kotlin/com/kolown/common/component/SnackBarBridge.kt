package com.kolown.common.component

import com.kolown.model.SnackBarData

class SnackBarBridge(
    private val onSnackBarDataAdded: (SnackBarData) -> Unit = {}
) {
    fun postSnackBarData(data: SnackBarData) {
        onSnackBarDataAdded(data)
    }
}
