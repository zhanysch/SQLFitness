package com.example.firebase.ui.main



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.firebase.MyLocationForegroundService
import com.example.firebase.R
import com.example.firebase.base.BaseMapActivity
import com.example.firebase.data.events.TraningEndedEvent
import com.example.firebase.data.events.UserLocationEvent
import com.example.firebase.ui.history.HistoryActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.view_bottom_sheet.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseMapActivity(), MainContract.View {
    override fun getResId() = R.layout.activity_main
    override fun getMapViewId() = R.id.mapView
    private  var presenter: MainPresenter? =null
   private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
        setupListeners()
        stopForeground()
        presenter = MainPresenter()
        presenter?.bind(this)
        presenter?.showLastRace()

    }


    override fun onDestroy() {
        presenter?.unbind()
        super.onDestroy()
    }


    private fun setupListeners() {
        fab.setOnClickListener {
            map?.setStyle(Style.SATELLITE_STREETS)
        }
        BtnStart.setOnClickListener {
            startForegroundService()
        }

        btnResult.setOnClickListener {
            startActivity(Intent(this , HistoryActivity::class.java))
        }
        BtnStop.setOnClickListener {
            stopForeground()
        }

        btnBottomSheet.setOnClickListener {
            presenter?.checkBsState(bottomSheetBehavior?.state)
        }
    }

    private fun setupBottomSheet(){
      bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback(){

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("fdgdf","fdds")
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("fdgdf","fdds")

            }
       })

    }



    private fun stopForeground() {
        val intent = Intent(this, MyLocationForegroundService::class.java)
        stopService(intent)
    }


    private fun startForegroundService() {
        val intent = Intent(this, MyLocationForegroundService::class.java)
        intent.putExtra("blabla", "blabla")
        startService(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  // в главном потоке
    fun getUserData(event: UserLocationEvent) {
        presenter?.byDirections(event.list)
        presenter?.collectDistance(event.distance)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  // в главном потоке
    fun endedTraning(event: TraningEndedEvent) {
        presenter?.saveTraning()
    }


    override fun viewShow(featureCollection: FeatureCollection) {
        getDirections(featureCollection)
    }

    override fun changeBsState(stateCollapsed: Int) {
       bottomSheetBehavior?.state = stateCollapsed
    }

    override fun showLastRoute(point: ArrayList<Point>) {
        presenter?.byDirections(point)
    }

    override fun onStart() {  // получаем событие
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {// не получаем событие
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}


