package com.kolown.camera.filter.common

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import android.util.Pair
import android.util.SparseArray
import android.view.TextureView.SurfaceTextureListener
import com.kolown.camera.R
import java.io.IOException
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface

class Renderer {
}

class CameraRenderer(private val context: Context) : Runnable, SurfaceTextureListener {
    private var renderThread: Thread? = null
    private var surfaceTexture: SurfaceTexture? = null
    private var gwidth = 0
    private var gheight = 0
    private var mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK

    private var eglDisplay: EGLDisplay? = null
    private var eglSurface: EGLSurface? = null
    private var eglContext: EGLContext? = null
    private var egl10: EGL10? = null

    private var camera: Camera? = null
    private var cameraSurfaceTexture: SurfaceTexture? = null
    private var cameraTextureId = 0
    private var selectedFilter: Filter? = null
    private var selectedFilterId: Filter
    private val cameraFilterMap: SparseArray<Filter> = SparseArray<Filter>()

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        gwidth = -width
        gheight = -height
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        if (camera != null) {
            camera!!.stopPreview()
            camera!!.release()
        }
        if (renderThread != null && renderThread!!.isAlive) {
            renderThread!!.interrupt()
        }
        CameraFilter.release()

        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        if (renderThread != null && renderThread!!.isAlive) {
            renderThread!!.interrupt()
        }
        renderThread = Thread(this)

        surfaceTexture = surface
        gwidth = -width
        gheight = -height

        // Open camera
        val cameraInfo = getCamera(mCameraFacing)
        val backCameraId = cameraInfo!!.second
        camera = Camera.open(backCameraId)

        // Start rendering
        renderThread!!.start()
    }

    /*
        자동으로 포커싱 객체를 잡는다
     */
    fun focusing() {
        try {
            camera!!.autoFocus { success, camera -> }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    fun setCameraFacing(cameraFacing: Int) {
        mCameraFacing = cameraFacing
    }

    fun setSelectedFilter(id: Int) {
        selectedFilterId = id
        selectedFilter = cameraFilterMap[id]
        if (selectedFilter != null) selectedFilter.onAttach()
    }

    override fun run() {
        initGL(surfaceTexture)

        // Setup camera filters map
        cameraFilterMap.append(R.id.filter_original, OriginalFilter(context))
        cameraFilterMap.append(R.id.black_white_default, BlackWhiteFilter(context))
        cameraFilterMap.append(R.id.black_white_bright, BlackWhiteBrightFilter(context))
        cameraFilterMap.append(R.id.black_white_dark, BlackWhiteDarkFilter(context))
        cameraFilterMap.append(R.id.filter_blue_orange, BlueorangeFilter(context))
        cameraFilterMap.append(R.id.filter_edge_detection, EdgeDetectionFilter(context))
        cameraFilterMap.append(R.id.filter_pixelize, PixelizeFilter(context))
        cameraFilterMap.append(R.id.filter_em_interference, EMInterferenceFilter(context))
        cameraFilterMap.append(R.id.filter_triangles_mosaic, TrianglesMosaicFilter(context))
        cameraFilterMap.append(R.id.filter_legofied, LegofiedFilter(context))
        cameraFilterMap.append(R.id.filter_tile_mosaic, TileMosaicFilter(context))
        cameraFilterMap.append(R.id.filter_chromatic_aberration, ChromaticAberrationFilter(context))
        cameraFilterMap.append(R.id.filter_basic_deform, BasicDeformFilter(context))
        cameraFilterMap.append(R.id.filter_contrast, ContrastFilter(context))
        cameraFilterMap.append(R.id.filter_noise_warp, NoiseWarpFilter(context))
        cameraFilterMap.append(R.id.filter_refraction, RefractionFilter(context))
        cameraFilterMap.append(R.id.filter_mapping, MappingFilter(context))
        cameraFilterMap.append(R.id.filter_crosshatch, CrosshatchFilter(context))
        cameraFilterMap.append(R.id.filter_lichtenstein_esque, LichtensteinEsqueFilter(context))
        cameraFilterMap.append(R.id.filter_ascii_art, AsciiArtFilter(context))
        cameraFilterMap.append(R.id.filter_money_filter, MoneyFilter(context))
        cameraFilterMap.append(R.id.filter_cracked, CrackedFilter(context))
        cameraFilterMap.append(R.id.filter_polygonization, PolygonizationFilter(context))
        cameraFilterMap.append(R.id.filter_jfa_voronoi, JFAVoronoiFilter(context))
        setSelectedFilter(selectedFilterId)

        // Create texture for camera preview
        cameraTextureId = MyGLUtils.genTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        cameraSurfaceTexture = SurfaceTexture(cameraTextureId)

        // Start camera preview
        try {
            camera!!.setPreviewTexture(cameraSurfaceTexture)
            camera!!.startPreview()
        } catch (ioe: IOException) {
            // Something bad happened
        }

        // Render loop
        while (!Thread.currentThread().isInterrupted) {
            try {
                if (gwidth < 0 && gheight < 0) GLES20.glViewport(
                    0,
                    0,
                    -gwidth.also { gwidth = it },
                    -gheight.also { gheight = it })

                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

                // Update the camera preview texture
                synchronized(this) {
                    cameraSurfaceTexture!!.updateTexImage()
                }

                // Draw camera preview
                val isFacingFront = mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT
                selectedFilter.draw(cameraTextureId, gwidth, gheight, isFacingFront)

                // Flush
                GLES20.glFlush()
                egl10!!.eglSwapBuffers(eglDisplay, eglSurface)

                Thread.sleep(DRAW_INTERVAL.toLong())
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }

        cameraSurfaceTexture!!.release()
        GLES20.glDeleteTextures(1, intArrayOf(cameraTextureId), 0)
    }

    private fun initGL(texture: SurfaceTexture?) {
        egl10 = EGLContext.getEGL() as EGL10

        eglDisplay = egl10!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        if (eglDisplay === EGL10.EGL_NO_DISPLAY) {
            throw RuntimeException(
                "eglGetDisplay failed " +
                        GLUtils.getEGLErrorString(egl10!!.eglGetError())
            )
        }

        val version = IntArray(2)
        if (!egl10!!.eglInitialize(eglDisplay, version)) {
            throw RuntimeException(
                "eglInitialize failed " +
                        GLUtils.getEGLErrorString(egl10!!.eglGetError())
            )
        }

        val configsCount = IntArray(1)
        val configs = arrayOfNulls<EGLConfig>(1)
        val configSpec = intArrayOf(
            EGL10.EGL_RENDERABLE_TYPE,
            EGL_OPENGL_ES2_BIT,
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_DEPTH_SIZE, 0,
            EGL10.EGL_STENCIL_SIZE, 0,
            EGL10.EGL_NONE
        )

        var eglConfig: EGLConfig? = null
        require(egl10!!.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)) {
            "eglChooseConfig failed " +
                    GLUtils.getEGLErrorString(egl10!!.eglGetError())
        }
        if (configsCount[0] > 0) {
            eglConfig = configs[0]
        }
        if (eglConfig == null) {
            throw RuntimeException("eglConfig not initialized")
        }

        val attrib_list = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        eglContext =
            egl10!!.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list)
        eglSurface = egl10!!.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null)

        if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
            val error = egl10!!.eglGetError()
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                Log.e(TAG, "eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW")
                return
            }
            throw RuntimeException(
                "eglCreateWindowSurface failed " +
                        GLUtils.getEGLErrorString(error)
            )
        }

        if (!egl10!!.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw RuntimeException(
                "eglMakeCurrent failed " +
                        GLUtils.getEGLErrorString(egl10!!.eglGetError())
            )
        }
    }

    private fun getCamera(facing: Int): Pair<Camera.CameraInfo, Int>? {
        val cameraInfo = Camera.CameraInfo()
        val numberOfCameras = Camera.getNumberOfCameras()

        for (i in 0 until numberOfCameras) {
            Camera.getCameraInfo(i, cameraInfo)
            if (cameraInfo.facing == facing) {
                return Pair(cameraInfo, i)
            }
        }
        return null
    }

    companion object {
        private const val TAG = "CameraRenderer"
        private const val EGL_OPENGL_ES2_BIT = 4
        private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
        private const val DRAW_INTERVAL = 1000 / 30
    }
}
