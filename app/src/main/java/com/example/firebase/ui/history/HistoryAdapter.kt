package com.example.firebase.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.data.model.MainTraning
import kotlinx.android.synthetic.main.activity_history_adapter.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val listener : ItemClicks):ListAdapter<MainTraning,HistoryVH>(DiffUtilCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_history_adapter,parent,false)
        return HistoryVH(view)
    }

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(getItem(position), listener)
    }

}
class DiffUtilCallBack: DiffUtil.ItemCallback<MainTraning>(){
    override fun areItemsTheSame(oldItem: MainTraning, newItem: MainTraning): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: MainTraning, newItem: MainTraning): Boolean {
        return oldItem.id == newItem.id
                && oldItem.finishAt == newItem.finishAt
                && oldItem == newItem
    }
}

class HistoryVH (view: View): RecyclerView.ViewHolder(view){
    fun bind(item: MainTraning, listener: ItemClicks) {



        itemView.TextFour.text = item.duration.toString()
        itemView.TextFive.text = item.calories.toString()

        itemView.BtnDelete.setOnClickListener {
            listener.clickDelete(item)
        }

        val dateTwo  = Date(item.startAt)
        val sfd = SimpleDateFormat("HH:mm:ss dd:MMMM:yyyy", Locale.getDefault())
        itemView.TextOne.text =
            itemView.context.resources.getString(R.string._13_12_12_30_09_20, sfd.format(dateTwo))

        val date  = Date(item.finishAt)
        val sdf = SimpleDateFormat("HH:mm:ss dd:MMMM:yyyy", Locale.getDefault())

        itemView.TextTwo.text =
            itemView.context.resources.getString(R.string._end_time,sdf.format(date))


        val df = DecimalFormat("0.000")
        itemView.TextThree.text =
            itemView.context.resources.getString(R.string._end_time,df.format(item.distance)
            )



    }

}