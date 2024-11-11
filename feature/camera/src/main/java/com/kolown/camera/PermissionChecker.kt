package com.kolown.camera

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat

object PermissionChecker {
    fun checkCameraPermission(context: Context): Boolean {
        val a = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        return a
    }

}
