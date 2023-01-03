package com.example.birday.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.domain.Event

class EventListAdapter: RecyclerView.Adapter<EventListAdapter.EventHolder>() {
    class EventHolder(view: View): RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val dateTag = view.findViewById<CardView>(R.id.date_tag)
    }

    var eventList = listOf<Event>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.person_item,
            parent,
            false
        )
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = eventList[position]
        holder.tvName.text = event.name
        holder.tvDate.text = event.date
        val visibility = if(event.showDateTag) View.VISIBLE else View.GONE
        holder.dateTag.visibility = visibility
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}