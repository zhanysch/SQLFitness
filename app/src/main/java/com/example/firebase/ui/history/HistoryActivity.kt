package com.example.firebase.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.firebase.FitnessApp
import com.example.firebase.R
import com.example.firebase.data.model.MainTraning
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    }

    private fun setupRecycler() {
        recyclerHistory.adapter = adapter

        FitnessApp.app?.getDB()?.getTraningDao()?.getTraningLiveData()?.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun clickDelete(item: MainTraning) {
        GlobalScope.launch {
           kotlin.runCatching {
               FitnessApp.app?.getDB()?.getTraningDao()?.deleteTraning(item.id)
           }
        }
    }
}