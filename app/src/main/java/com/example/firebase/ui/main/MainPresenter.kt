package com.example.firebase.ui.main

import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

class MainPresenter: MainContract.Presenter {

    private var view: MainContract.View? = null

    override fun byDirections(latLng: ArrayList<Point>) {
        val lineString = LineString.fromLngLats(latLng)
        val featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString))
      /*  source.let {geoJsonsource ->
            geoJsonsource?.setGeoJson(featureCollection)
        }*/
    }

    override fun bind(view: MainContract.View) {
        this.view = view

    }

    override fun unbind() {
        this.view = null
    }

}