package com.example.firebase.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.firebase.data.model.LatlngPoints
import com.example.firebase.data.model.MainTraning

@Dao
interface TraningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTraning(data : MainTraning)

    @Dao
   interface TraningSave{
        @Insert(onConflict =  OnConflictStrategy.REPLACE)
        fun saveTraning (dataTwo : LatlngPoints )
   }
}