package com.example.firebase.ui.main

import com.example.firebase.FitnessApp
import com.example.firebase.data.model.LatlngPoints
import com.example.firebase.data.model.MainTraning
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.*

class MainPresenter: MainContract.Presenter {

    private var view: MainContract.View? = null
    private val job = Job()
   private val scope = CoroutineScope(job)

    override fun byDirections(list: ArrayList<Point>) {
        saveInDB(list)
        val lineString = LineString.fromLngLats(list)
        val featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString))
        view?.viewShow(featureCollection)
    }

    private fun saveInDB(list: ArrayList<Point>) {
      scope.launch(Dispatchers.Default) {
          val data = getTraningModel(list)
          FitnessApp.app?.getDB()?.getTraningDao()?.addTraning(data)
        /* val tran = Save(list)
          FitnessApp.app?.getDB()?.getTraningDao()?.TraningSave(tran)*/
      }
    }

    private fun getTraningModel(list: ArrayList<Point>): MainTraning {
       return MainTraning(
           point = LatlngPoints(point = list),
           distance = 132,
           duration = 143,
           startAt = "df",
           finishAt = "ggd",
           calories = 12
       )
    }
    /*private fun Save(list: ArrayList<Point>) {
        point = list
    }
*/
    override fun bind(view: MainContract.View) {
        this.view = view
    }

    override fun unbind() {
        scope.cancel()
        this.view = null
    }

}