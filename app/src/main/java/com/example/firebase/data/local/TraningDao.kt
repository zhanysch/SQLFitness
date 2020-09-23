package com.example.firebase.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.example.firebase.data.model.MainTraning

@Dao
interface TraningDao {
    @Insert
    fun addTraning(data : MainTraning)
}