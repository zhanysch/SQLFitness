package com.example.firebase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MainTraning (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0 ,
    val  point : LatlngPoints,
    val distance : Int,
    val duration : Int,
    val startAt: String,
    val finishAt: String,
    val calories: Int



)