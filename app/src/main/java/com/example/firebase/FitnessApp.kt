package com.example.firebase

import android.app.Application
import com.example.firebase.data.PreferenceHelper

class FitnessApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.initPreference(applicationContext)
    }

}