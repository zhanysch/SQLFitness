package com.example.firebase.ui.history

import com.example.firebase.FitnessApp
import com.example.firebase.data.model.MainTraning
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryPresenter: HistoryContract.Presenter {

    private var view : HistoryContract.View? = null


    override fun bind(view: HistoryContract.View) {
        this.view = view
    }

    override fun unbind() {
      this.view= null
    }



    override fun byDelete(item: MainTraning) {
        GlobalScope.launch {
            kotlin.runCatching {
                FitnessApp.app?.getDB()?.getTraningDao()?.deleteTraning(item.id)
            }
        }
    }

}