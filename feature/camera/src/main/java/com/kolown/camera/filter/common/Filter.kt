package com.kolown.camera.filter.common

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import androidx.annotation.CallSuper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


abstract class Filter(context: Context) {
    val squareCoords = floatArrayOf(1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f)
    val textureCoords = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
    val vertexBuf: FloatBuffer
    val textureCoordBuf: FloatBuffer
    open var program: Int = 0
    val bufferActiveTextureUnit = GLES20.GL_TEXTURE0
    var cameraRenderBuffer: RenderBuffer? = null

    //후면 카메라 회전된 좌표
    private val backCameraRotatedTextureCoords =
        floatArrayOf(1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    //정면 카메라 회전된 좌표
    private val frontCameraRotatedTextureCoords =
        floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
    private val frontCameraRotatedTextureCoordBuf: FloatBuffer
    private val backCameraRotatedTextureCoordBuf: FloatBuffer

    val startTime = System.currentTimeMillis()
    var iFrame = 0

    init {
        vertexBuf = ByteBuffer.allocateDirect(squareCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexBuf.put(squareCoords)
        vertexBuf.position(0)

        textureCoordBuf = ByteBuffer.allocateDirect(textureCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        textureCoordBuf.put(textureCoords)
        textureCoordBuf.position(0)

        frontCameraRotatedTextureCoordBuf =
            ByteBuffer.allocateDirect(frontCameraRotatedTextureCoords.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        frontCameraRotatedTextureCoordBuf.put(frontCameraRotatedTextureCoords)
        frontCameraRotatedTextureCoordBuf.position(0)

        backCameraRotatedTextureCoordBuf =
            ByteBuffer.allocateDirect(backCameraRotatedTextureCoords.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        backCameraRotatedTextureCoordBuf.put(backCameraRotatedTextureCoords)
        backCameraRotatedTextureCoordBuf.position(0)


        //override하는 자식이 super를 무조건 call 하게 강제하는 annotation

    }

    @CallSuper
    fun onAttach() {
        iFrame = 0
    }

    fun draw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int, isFacingFront: Boolean) {
        // Create camera render buffer
        if (cameraRenderBuffer == null ||
            cameraRenderBuffer?.width != canvasWidth ||
            cameraRenderBuffer?.height != canvasHeight
        ) {
            cameraRenderBuffer = RenderBuffer(
                canvasWidth,
                canvasHeight,
                bufferActiveTextureUnit
            )
        }

        // Use shaders
        GLES20.glUseProgram(program)

        val iChannel0Location = GLES20.glGetUniformLocation(
            program,
            "iChannel0"
        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexId)
        GLES20.glUniform1i(iChannel0Location, 0)

        val vPositionLocation = GLES20.glGetAttribLocation(
            program,
            "vPosition"
        )
        GLES20.glEnableVertexAttribArray(vPositionLocation)
        GLES20.glVertexAttribPointer(
            vPositionLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            vertexBuf
        )

        val vTexCoordLocation = GLES20.glGetAttribLocation(
            program,
            "vTexCoord"
        )
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        if (isFacingFront) {
            GLES20.glVertexAttribPointer(
                vTexCoordLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                4 * 2,
                frontCameraRotatedTextureCoordBuf
            )
        } else {
            GLES20.glVertexAttribPointer(
                vTexCoordLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                4 * 2,
                backCameraRotatedTextureCoordBuf
            )
        }

        // Render to texture
        cameraRenderBuffer?.bind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        cameraRenderBuffer?.unbind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        cameraRenderBuffer?.let {
            onDraw(
                it.texId,
                canvasWidth,
                canvasHeight
            )
        }


        iFrame++
    }

    abstract fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int)

    fun setupShaderInputs(
        program: Int,
        iResolution: IntArray,
        iChannels: IntArray,
        iChannelResolutions: Array<IntArray>
    ) {
        setupShaderInputs(
            program,
            vertexBuf,
            textureCoordBuf,
            iResolution,
            iChannels,
            iChannelResolutions
        )
    }

    fun setupShaderInputs(
        program: Int,
        vertex: FloatBuffer?,
        textureCoord: FloatBuffer?,
        iResolution: IntArray,
        iChannels: IntArray,
        iChannelResolutions: Array<IntArray>
    ) {
        GLES20.glUseProgram(program)

        val iResolutionLocation = GLES20.glGetUniformLocation(program, "iResolution")
        GLES20.glUniform3fv(
            iResolutionLocation, 1,
            FloatBuffer.wrap(floatArrayOf(iResolution[0].toFloat(), iResolution[1].toFloat(), 1.0f))
        )

        val time = ((System.currentTimeMillis() - startTime).toFloat()) / 1000.0f
        val iGlobalTimeLocation = GLES20.glGetUniformLocation(program, "iGlobalTime")
        GLES20.glUniform1f(iGlobalTimeLocation, time)

        val iFrameLocation = GLES20.glGetUniformLocation(program, "iFrame")
        GLES20.glUniform1i(iFrameLocation, iFrame)

        val vPositionLocation = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(vPositionLocation)
        GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, vertex)

        val vTexCoordLocation = GLES20.glGetAttribLocation(program, "vTexCoord")
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        GLES20.glVertexAttribPointer(
            vTexCoordLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            textureCoord
        )

        for (i in iChannels.indices) {
            val sTextureLocation = GLES20.glGetUniformLocation(program, "iChannel$i")
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iChannels[i])
            GLES20.glUniform1i(sTextureLocation, i)
        }

        val _iChannelResolutions = FloatArray(iChannelResolutions.size * 3)
        for (i in iChannelResolutions.indices) {
            _iChannelResolutions[i * 3] = iChannelResolutions[i][0].toFloat()
            _iChannelResolutions[i * 3 + 1] = iChannelResolutions[i][1].toFloat()
            _iChannelResolutions[i * 3 + 2] = 1.0f
        }

        val iChannelResolutionLocation = GLES20.glGetUniformLocation(program, "iChannelResolution")
        GLES20.glUniform3fv(
            iChannelResolutionLocation,
            _iChannelResolutions.size, FloatBuffer.wrap(_iChannelResolutions)
        )
    }

    fun release() {
        program = 0
        GLES20.glDeleteProgram(program)
    }
}
