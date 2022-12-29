package com.example.birday.domain

import androidx.lifecycle.LiveData

interface EventListRepository {

    fun addEvent(event: Event)

    fun deleteEvent(event: Event)

    fun editEvent(event: Event)

    fun getEventById(id: Int): Event?

    fun getEventList(): LiveData<List<Event>>
}