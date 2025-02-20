package com.example.lincrideapp.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LincRideApp : Application() {

    companion object {
        @get:Synchronized
        lateinit var application: LincRideApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}