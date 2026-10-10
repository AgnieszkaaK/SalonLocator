package com.setu.salonlocatorapp

import android.app.Application
import com.setu.salonlocatorapp.models.SalonJSONStore
// sets up your salon storage when android starts the app’s process
class SalonApplication : Application() {
//creates own application class and inherits android's application behaviour
    override fun onCreate() {
        super.onCreate()

        // creates the JSON store and loads saved salons before screens open
        AppData.salons = SalonJSONStore(this)
    }
}

//application class creates the JSON store at startup. The store loads saved salons, and all screens access it through AppData