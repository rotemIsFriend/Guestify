package com.example.guestify.di

import android.app.Application
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuestifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
            .metaData.getString("com.google.android.geo.API_KEY")
        if(!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }
    }
}
