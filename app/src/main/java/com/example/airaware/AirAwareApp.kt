package com.example.airaware

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class AirAwareApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        Log.d("Air Aware App", "Firebase initialized correctly")
    }
}