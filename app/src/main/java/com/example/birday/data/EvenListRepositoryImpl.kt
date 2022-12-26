package com.example.birday.data

import com.example.birday.domain.Event
import com.example.birday.domain.EventListRepository

object EvenListRepositoryImpl : EventListRepository {

    private val eventList = mutableListOf<Event>()

    private var autoIncrementId = 0

    override fun addEvent(event: Event) {
        if (event.id == Event.UNDEFINED_ID) event.id = autoIncrementId++
        eventList.add(event)
    }

    override fun deleteEvent(event: Event) {
        eventList.remove(event)
    }

    override fun editEvent(event: Event) {
        val oldItem = getEventById(event.id)
        eventList.remove(oldItem)
        addEvent(event)
    }

    override fun getEventById(id: Int): Event? {
        return eventList.find { it.id == id }
    }

    override fun getEventList(): List<Event> {
        return eventList.toList()
    }
}