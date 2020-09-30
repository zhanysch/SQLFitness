package com.example.firebase.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firebase.data.model.LatlngPoints
import com.example.firebase.data.model.MainTraning

@Dao
interface TraningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTraning(data : MainTraning)

    @Query("SELECT * FROM MainTraning")
    fun getTraning(): List<MainTraning>

    @Query("SELECT * FROM MainTraning")
    fun getTraningLiveData(): LiveData<List<MainTraning>>


    @Query("DELETE  FROM MainTraning WHERE id = :id")
    fun deleteTraning(id : Int)

}