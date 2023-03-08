package com.example.birday.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.birday.domain.Event
import com.example.birday.domain.EventListRepository
import com.example.birday.presentation.EventListAdapter
import java.time.LocalDate
import java.time.Period
import java.util.TreeSet
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
object EventListRepositoryImpl : EventListRepository {

    private val eventListLD = MutableLiveData<List<Event>>()
    private val firstEventLD = MutableLiveData<Event>()


    private val eventList = sortedSetOf<Event>(
        { o1, o2 -> o2.daysLeft() compareTo(o1.daysLeft()) }
    )
    //private val eventList = mutableListOf<Event>()

    //TODO: Эта залупа удаляет дубликаты, исправить

    private var autoIncrementId = 0
    init {
        for (i in 0 until 20) {
            val date = getRandomDate()
            addEvent(Event("firstName$i",
                "lastName$i",
                date))
        }
        addEvent(Event("TODAY","",LocalDate.now()))
    }

    private fun getRandomDate(): LocalDate{
        val day = Random.nextInt(1,29)
        val month = Random.nextInt(1,13)
        val year = Random.nextInt(2000, 2024)
        return LocalDate.of(year,month,day)
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
    }

    override fun getEventById(id: Int): Event? {
        return eventList.find { it.id == id }
    }

    override fun getEventList(): LiveData<List<Event>> {
        return eventListLD
    }

    override fun getListSize(): Int {
        return eventList.size
    }

    override fun getFirstEvent(): LiveData<Event> {
        return firstEventLD
    }
    private fun updateList() {
        var month = 0

        //eventList.sortByDescending { it.daysLeft() }

        for(i in eventList){
            i.showDateTag = i.date.monthValue != month
            month = i.date.monthValue
        }

        eventListLD.value = eventList.toList()
        if(eventListLD.value!!.isNotEmpty()) firstEventLD.value = eventList.first()
    }
}
