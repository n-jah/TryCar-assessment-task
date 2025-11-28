package com.example.trycar_assessment_task

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application started")
    }

    companion object {
        private const val TAG = "MyApp"
    }
}
