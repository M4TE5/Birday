package com.example.birday.presentation

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.domain.Event
import java.time.format.DateTimeFormatter
import java.util.*

class FavoriteEventListAdapter :
    ListAdapter<Event, FavoriteEventListAdapter.FavoriteEventHolder>(EventDiffCallBack()) {
    class FavoriteEventHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvNextCelebrate: TextView = view.findViewById(R.id.tv_next_celebrate)
        val tvYears: TextView = view.findViewById(R.id.tv_years)
        val tvDaysLeft: TextView = view.findViewById(R.id.tv_days_left)
    }

    var onEventClickListener: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_event_item, parent, false)
        return FavoriteEventHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FavoriteEventHolder, position: Int) {
        val event = getItem(position)
        val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
        holder.tvName.text = "${event.firstName} ${event.lastName}"
        val dayName = event.date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val date = event.date.format(dateFormatter)

        holder.tvDaysLeft.text = Event.daysLeft(event.date).toString()

        holder.tvNextCelebrate.text = "$dayName, $date"
        holder.itemView.setOnClickListener {
            onEventClickListener?.invoke(event)
        }
    }

}