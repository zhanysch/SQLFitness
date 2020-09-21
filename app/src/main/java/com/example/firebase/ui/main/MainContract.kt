package com.example.firebase.ui.main

import com.example.firebase.ui.LiveCycle
import com.mapbox.geojson.Point

interface MainContract {
    interface View {

    }

    interface Presenter : LiveCycle<View>{
        fun byDirections(latLng: ArrayList<Point>)

    }
}