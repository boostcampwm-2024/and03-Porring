package com.kolown.camera

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext()= runTest{
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Log.e("test","테스트 시작")
        delay(5000)
        Log.e("test","테스트 끝")
        advanceUntilIdle()
        assertEquals("com.kolown.camera.test", appContext.packageName)

    }
}
