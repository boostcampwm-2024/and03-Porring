package com.kolown.porring

import android.app.Application
import android.content.Intent
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltAndroidApp
class PorringApplication : Application(){
    companion object {
        private lateinit var instance: PorringApplication
        fun getApplicationContext(): PorringApplication = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}







