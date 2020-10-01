package com.example.firebase.ui.main

import android.util.Log
import com.example.firebase.FitnessApp
import com.example.firebase.data.events.UserLocationEvent
import com.example.firebase.data.model.LatlngPoints
import com.example.firebase.data.model.MainTraning
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.*

class MainPresenter: MainContract.Presenter {

    private var view: MainContract.View? = null
    private val job = Job()
   private val scope = CoroutineScope(job)
    private var list = arrayListOf<Point>()//1
    private var distance : Double = 0.0
    private var startTime : Long = 0

    override fun byDirections(list: ArrayList<Point>) {
        if (list.size == 1) saveCurrentTime()
        this.list = list//1
        val lineString = LineString.fromLngLats(list)
        val featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString))
        view?.viewShow(featureCollection)
    }

    override fun collectDistance(distance: Double) {
        this.distance = distance
    }

    override fun checkBsState(state: Int?) {
        if (state == BottomSheetBehavior.STATE_EXPANDED) {   //  если разверн состоние
            view?.changeBsState(BottomSheetBehavior.STATE_COLLAPSED)

        }  else if (state == BottomSheetBehavior.STATE_COLLAPSED){   // colapsed скрыть
            view?.changeBsState(BottomSheetBehavior.STATE_EXPANDED)
        }

    }

    override fun showLastRace() {  // вставляет
        scope.launch(Dispatchers.Default) {
            val data  = FitnessApp.app?.getDB()?.getTraningDao()?.getTraning()
            Log.d("fdddgfd", "fdsfs")

           // data?.point?.point?.let { view?.showLastRoute(it) }
        }
    }

    override fun saveTraning() {
        saveInDB(list)  // 1
    }

    override fun saveCurrentTime() {
        startTime = System.currentTimeMillis()
    }

    private fun saveInDB(list: ArrayList<Point>) { // сохраняет
      scope.launch(Dispatchers.Default) {
          val data = getTraningModel(list)
          FitnessApp.app?.getDB()?.getTraningDao()?.addTraning(data)
      }
    }

    private fun getTraningModel(list: ArrayList<Point>): MainTraning {
       return MainTraning(
           point = LatlngPoints(point = list),
           distance = distance,
           duration = System.currentTimeMillis() - startTime,
           startAt = startTime,
           finishAt = System.currentTimeMillis(),
           calories = 12
       )
    }


    override fun bind(view: MainContract.View) {
        this.view = view
    }

    override fun unbind() {
        scope.cancel()
        this.view = null
    }

}