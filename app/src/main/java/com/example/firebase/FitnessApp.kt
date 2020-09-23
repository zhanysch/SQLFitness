package com.example.firebase

import android.app.Application
import androidx.room.Room
import com.example.firebase.data.local.PreferenceHelper
import com.example.firebase.data.local.TraningDataBase

class FitnessApp: Application() {

    private var db : TraningDataBase? = null

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.initPreference(applicationContext)
        app = this
        db = Room.databaseBuilder(applicationContext,TraningDataBase::class.java, DB_NAME)
            
            .build()
    }

    fun getDB() = db

    companion object{
        private var app : FitnessApp? = null
        private const val DB_NAME = "MY_DB"
    }

}