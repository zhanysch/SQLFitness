package com.example.firebase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.firebase.data.model.LatlngPoints
import com.example.firebase.data.model.MainTraning

@TypeConverters(value = [TraningTypeConverter::class])
@Database(entities = [MainTraning::class, LatlngPoints::class],version = 1)
abstract class TraningDataBase: RoomDatabase() {
    abstract fun getTraningDao(): TraningDao
}