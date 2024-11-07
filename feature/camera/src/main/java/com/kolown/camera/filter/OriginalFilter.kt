package com.kolown.camera.filter

import android.content.Context
import com.kolown.camera.R
import com.kolown.camera.filter.common.Filter
import com.kolown.camera.filter.util.GLUtil

class OriginalFilter(context:Context):Filter(context){
    private val innerProgram: Int = GLUtil.buildProgram(context, R.raw.vertext, R.raw.original)


    override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(innerProgram,
            intArrayOf(canvasWidth,canvasHeight),
            intArrayOf(cameraTexId),
            arrayOf()
        )
    }
}
