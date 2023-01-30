package com.example.birday.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.AddEventUseCase
import com.example.birday.domain.EditEventUseCase
import com.example.birday.domain.Event
import com.example.birday.domain.GetEventByIdUseCase

class EventItemViewModel: ViewModel() {
    private val repository = EventListRepositoryImpl

    private val addEventUseCase = AddEventUseCase(repository)
    private val editEventUseCase = EditEventUseCase(repository)
    private val getEventUseCase = GetEventByIdUseCase(repository)

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    fun addItem(firstName: String, lastName: String, date: String){
        val event = Event(firstName + lastName, date)
        addEventUseCase.addEvent(event)
    }

    fun editItem(firstName: String, lastName: String, date: String){
        _event.value?.let {
            val event = it.copy(name = (firstName + lastName), date = date)
            editEventUseCase.editEvent(event)
        }
    }

    fun getItemById(id: Int){
        val item = getEventUseCase.getEventById(id)!!
        _event.value = item
    }

}