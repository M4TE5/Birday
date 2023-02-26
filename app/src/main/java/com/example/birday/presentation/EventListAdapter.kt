package com.example.birday.presentation

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.databinding.EventItemBinding
import com.example.birday.domain.Event
import java.time.format.DateTimeFormatter

class EventListAdapter: ListAdapter<Event, EventListAdapter.EventHolder>(EventDiffCallBack()){
    class EventHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = EventItemBinding.bind(view)
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

        holder.binding.apply {
            tvName.text = "${event.firstName} ${event.lastName}"
            tvDate.text = dateStr
            checkBox.isChecked = event.favorite

            tvDateTag.text = "${event.date.month} - ${event.getNextCelebrationDate().year}"

            val visibility = if (event.showDateTag) View.VISIBLE else View.GONE
            dateTag.visibility = visibility

            checkBox.setOnClickListener {
                onCheckBoxChangeListener?.invoke(event, checkBox.isChecked)
            }

            constraintItem.setOnClickListener {
                onEventClickListener?.invoke(event)
            }
        }
    }
}