package com.teeno.tteapp

import android.app.Application
import com.google.firebase.FirebaseApp

class TteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
