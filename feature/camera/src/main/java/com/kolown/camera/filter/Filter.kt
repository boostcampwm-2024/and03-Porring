package com.kolown.camera.filter

import android.content.Context
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


abstract class Filter(context: Context) {
    val squareCoords = floatArrayOf(1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f)
    val textureCoords = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
    val vertexBuf: FloatBuffer
    val textureCoordBuf: FloatBuffer
    val program: Int = 0
    val bufferActiveTextureUnit = GLES20.GL_TEXTURE0
    val cameraRenderBuffer: RenderBuffer

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

        textureCoordBuf=ByteBuffer.allocateDirect(textureCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        textureCoordBuf.put(textureCoords)
        textureCoordBuf.position(0)

        frontCameraRotatedTextureCoordBuf = ByteBuffer.allocateDirect(frontCameraRotatedTextureCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        frontCameraRotatedTextureCoordBuf.put(frontCameraRotatedTextureCoords)
        frontCameraRotatedTextureCoordBuf.position(0)

        backCameraRotatedTextureCoordBuf = ByteBuffer.allocateDirect(backCameraRotatedTextureCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        backCameraRotatedTextureCoordBuf.put(backCameraRotatedTextureCoords)
        backCameraRotatedTextureCoordBuf.position(0)


    }
}
