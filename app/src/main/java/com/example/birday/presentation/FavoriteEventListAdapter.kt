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
import com.example.birday.databinding.FavoriteEventItemBinding
import com.example.birday.domain.Event
import java.time.format.DateTimeFormatter
import java.util.*

class FavoriteEventListAdapter : ListAdapter<
        Event,
        FavoriteEventListAdapter.FavoriteEventHolder
        >(EventDiffCallBack()) {
    class FavoriteEventHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = FavoriteEventItemBinding.bind(view)
    }

    var onEventClickListener: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.favorite_event_item,
                parent,
                false
            )
        return FavoriteEventHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FavoriteEventHolder, position: Int) {
        val event = getItem(position)
        val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
        val dayName = event.getDayName()
        val dateStr = event.getNextCelebrationDate().format(dateFormatter)

        holder.binding.apply {
            tvName.text = "${event.firstName} ${event.lastName}"
            tvDaysLeft.text = event.daysLeft().toString()
            tvNextCelebrate.text = "$dayName, $dateStr"
            tvYears.text = "Years: ${event.getAge()}, born in ${event.date.year}"
        }

        holder.itemView.setOnClickListener {
            onEventClickListener?.invoke(event)
        }
    }

}