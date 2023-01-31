package com.example.birday.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.birday.domain.Event
import com.example.birday.domain.EventListRepository
import kotlin.jvm.internal.Ref
import kotlin.random.Random

object EventListRepositoryImpl : EventListRepository {

    private val eventListLD = MutableLiveData<List<Event>>()

    private val eventList = sortedSetOf<Event>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private var autoIncrementId = 0

    init {
        for (i in 0 until 50) {
            addEvent(Event("firstName$i",
                "lastName$i",
                "$i",
                i % 5 == 0,
                i % 5 == 0))
        }
    }

    override fun addEvent(event: Event) {
        if (event.id == Event.UNDEFINED_ID) event.id = autoIncrementId++
        eventList.add(event)
        updateList()
    }

    override fun deleteEvent(event: Event) {
        eventList.remove(event)
        updateList()
    }

    override fun editEvent(event: Event) {
        val oldItem = getEventById(event.id)
        eventList.remove(oldItem)
        addEvent(event)
        updateList()
    }

    override fun getEventById(id: Int): Event? {
        return eventList.find { it.id == id }
    }

    override fun getEventList(): LiveData<List<Event>> {
        return eventListLD
    }

    private fun updateList() {
        eventListLD.value = eventList.toList()
    }
}