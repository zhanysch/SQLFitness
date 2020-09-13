package com.example.firebase.ui



import android.os.Bundle
import com.example.firebase.R
import com.mapbox.mapboxsdk.maps.Style
import com.ok.ui.BaseMapActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseMapActivity() {
    override fun getResId() = R.layout.activity_main
    override fun getMapViewId() = R.id.mapView
    val baish = BaseMapActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()



    }

    private fun setupListeners() {
        fab.setOnClickListener {
            map?.setStyle(Style.SATELLITE_STREETS)

        }
    }



}