package com.example.firebase.ui.main

import com.example.firebase.ui.LiveCycle
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point

interface MainContract {
    interface View {
        fun viewShow(featureCollection: FeatureCollection)
        fun changeBsState(stateCollapsed: Int)
        fun showLastRoute(point: ArrayList<Point>)

    }

    interface Presenter : LiveCycle<View>{
        fun byDirections(list:  ArrayList<Point>)
        fun checkBsState(state: Int?)
        fun showLastRace()
        fun saveTraning()
        fun saveCurrentTime()

    }
}