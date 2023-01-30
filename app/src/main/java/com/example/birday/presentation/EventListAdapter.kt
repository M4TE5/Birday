package com.example.birday.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.domain.Event

class EventListAdapter: ListAdapter<Event, EventListAdapter.EventHolder>(EventDiffCallBack()){
    class EventHolder(view: View): RecyclerView.ViewHolder(view){
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val dateTag: CardView = view.findViewById(R.id.date_tag)
        val checkBox: CheckBox = view.findViewById(R.id.check_box)
        val constraintItem: ConstraintLayout = view.findViewById(R.id.constraint_item)
    }

    var onEventClickListener: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = getItem(position)
        holder.tvName.text = event.name
        holder.tvDate.text = event.date
        holder.checkBox.isChecked = event.favorite
        val visibility = if (event.showDateTag) View.VISIBLE else View.GONE
        holder.dateTag.visibility = visibility
        holder.constraintItem.setOnClickListener {
            onEventClickListener?.invoke(event)
        }
    }

}