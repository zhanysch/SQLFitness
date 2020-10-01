package com.example.firebase.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.firebase.FitnessApp
import com.example.firebase.R
import com.example.firebase.data.model.MainTraning
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity(), ItemClicks, HistoryContract.View {

    private val adapter by lazy {
        HistoryAdapter(this)
    }
    private var presenter : HistoryPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupRecycler()
        presenter = HistoryPresenter()
        presenter?.bind(this)
    }

    private fun setupRecycler() {
        recyclerHistory.adapter = adapter
        FitnessApp.app?.getDB()?.getTraningDao()?.getTraningLiveData()?.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun clickDelete(item: MainTraning) {
        presenter?.byDelete(item)

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unbind()
    }
}