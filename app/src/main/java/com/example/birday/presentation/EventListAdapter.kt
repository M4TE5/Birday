package com.example.birday.presentation

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.domain.Event
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventListAdapter: ListAdapter<Event, EventListAdapter.EventHolder>(EventDiffCallBack()){
    class EventHolder(view: View): RecyclerView.ViewHolder(view){
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val dateTag: CardView = view.findViewById(R.id.date_tag)
        val checkBox: CheckBox = view.findViewById(R.id.check_box)
        val constraintItem: ConstraintLayout = view.findViewById(R.id.constraint_item)
    }

    var onEventClickListener: ((Event) -> Unit)? = null
    var onCheckBoxChangeListener: ((Event, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = getItem(position)

        val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
        val dateStr = event.date.format(dateFormatter)

        holder.tvName.text = "${event.firstName} ${event.lastName}"
        holder.tvDate.text = dateStr
        holder.checkBox.isChecked = event.favorite
        val visibility = if (event.showDateTag) View.VISIBLE else View.GONE
        holder.dateTag.visibility = visibility


        holder.checkBox.setOnClickListener {
            onCheckBoxChangeListener?.invoke(event, holder.checkBox.isChecked)
        }

        holder.constraintItem.setOnClickListener {
            onEventClickListener?.invoke(event)
        }
    }

}