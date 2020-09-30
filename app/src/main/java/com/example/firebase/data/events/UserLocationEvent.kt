package com.example.firebase.data.events

import com.mapbox.geojson.Point


data class UserLocationEvent (
    val list : ArrayList<Point>
)
data class TraningEndedEvent (
    val isFinished : Boolean
)