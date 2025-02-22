package com.example.guestify.di

import android.app.Application
import android.content.pm.PackageManager
import com.example.guestify.BuildConfig
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuestifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = BuildConfig.MAPS_API_KEY

        if(!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }
    }
}
