package com.kolown.camera.filter.util

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.microedition.khronos.opengles.GL10

object GLUtil {
    private const val TAG = "MyGLUtils"

    @JvmOverloads
    fun genTexture(textureType: Int = GLES20.GL_TEXTURE_2D): Int {
        val genBuf = IntArray(1)
        GLES20.glGenTextures(1, genBuf, 0)
        GLES20.glBindTexture(textureType, genBuf[0])

        // Set texture default draw parameters
        if (textureType == GLES11Ext.GL_TEXTURE_EXTERNAL_OES) {
            GLES20.glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            GLES20.glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            GLES20.glTexParameteri(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE
            )
        } else {
            GLES20.glTexParameterf(
                GLES20.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            GLES20.glTexParameterf(
                GLES20.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT)
        }

        return genBuf[0]
    }

    fun loadTexture(context: Context, resourceId: Int, size: IntArray): Int {
        val texId = genTexture()

        if (texId != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false // No pre-scaling
            options.inJustDecodeBounds = true

            // Just decode bounds
            BitmapFactory.decodeResource(context.resources, resourceId, options)

            // Set return size
            size[0] = options.outWidth
            size[1] = options.outHeight

            // Decode
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle()
        }

        return texId
    }

    fun buildProgram(context: Context, vertexSourceRawId: Int, fragmentSourceRawId: Int): Int {
        return buildProgram(
            getStringFromRaw(context, vertexSourceRawId),
            getStringFromRaw(context, fragmentSourceRawId)
        )
    }

    fun buildProgram(vertexSource: String?, fragmentSource: String?): Int {
        val vertexShader = buildShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }

        val fragmentShader = buildShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragmentShader == 0) {
            return 0
        }

        val program = GLES20.glCreateProgram()
        if (program == 0) {
            return 0
        }

        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        return program
    }

    fun buildShader(type: Int, shaderSource: String?): Int {
        val shader = GLES20.glCreateShader(type)
        if (shader == 0) {
            return 0
        }

        GLES20.glShaderSource(shader, shaderSource)
        GLES20.glCompileShader(shader)

        val status = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
        if (status[0] == 0) {
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader))
            GLES20.glDeleteShader(shader)
            return 0
        }

        return shader
    }

    private fun getStringFromRaw(context: Context, id: Int): String {
        var str: String
        try {
            val r = context.resources
            val `is` = r.openRawResource(id)
            val baos = ByteArrayOutputStream()
            var i = `is`.read()
            while (i != -1) {
                baos.write(i)
                i = `is`.read()
            }

            str = baos.toString()
            `is`.close()
        } catch (e: IOException) {
            str = ""
        }

        return str
    }
}
