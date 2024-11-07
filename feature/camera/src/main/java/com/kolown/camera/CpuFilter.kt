package com.kolown.camera

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.camera.core.ImageProxy

object CpuFilter {


    fun toGrayscale(imageProxy: ImageProxy): Bitmap {
        val bitmap=imageProxy.toBitmap()
        val c = Canvas(bitmap)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.setColorFilter(f)
        c.drawBitmap(bitmap, 0f, 0f, paint)
        return bitmap
    }
}
