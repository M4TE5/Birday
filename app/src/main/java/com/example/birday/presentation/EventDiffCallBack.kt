package com.example.birday.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.birday.domain.Event

class EventDiffCallBack : DiffUtil.ItemCallback<Event>(){
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

}