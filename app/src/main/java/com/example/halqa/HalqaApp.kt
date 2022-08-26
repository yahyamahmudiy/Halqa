package com.example.halqa

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HalqaApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}