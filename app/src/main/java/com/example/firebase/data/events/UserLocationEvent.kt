package com.example.firebase.data.events

import com.mapbox.geojson.Point


data class UserLocationEvent (
    val list : ArrayList<Point>,
    val distance : Double

)
data class TraningEndedEvent (
    val isFinished : Boolean
)