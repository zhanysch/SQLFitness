package com.example.firebase.ui.history

import com.example.firebase.data.model.MainTraning
import com.example.firebase.ui.LiveCycle

interface HistoryContract {

    interface View{

    }

    interface Presenter: LiveCycle<View> {
        fun byDelete(item: MainTraning)
    }
}