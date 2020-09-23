package com.example.firebase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.Point

@Entity
data class LatlngPoints (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0 ,
    val point : ArrayList<Point>

)