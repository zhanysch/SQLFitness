package com.example.firebase.ui.main

import com.example.firebase.ui.LiveCycle
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point

interface MainContract {
    interface View {
        fun viewShow(featureCollection: FeatureCollection)

    }

    interface Presenter : LiveCycle<View>{
        fun byDirections(list:  ArrayList<Point>)

    }
}