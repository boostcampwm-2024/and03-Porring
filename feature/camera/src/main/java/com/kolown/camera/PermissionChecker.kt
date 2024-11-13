package com.kolown.camera

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat


//common에 들어갈 예정
object PermissionChecker {
    fun checkCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

}
