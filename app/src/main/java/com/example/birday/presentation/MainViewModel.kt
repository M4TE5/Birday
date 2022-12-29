package com.example.birday.presentation

import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.DeleteEventUseCase
import com.example.birday.domain.EditEventUseCase
import com.example.birday.domain.Event
import com.example.birday.domain.GetEventListUseCase

class MainViewModel: ViewModel() {
    private val repository = EventListRepositoryImpl

    private val getEventListUseCase = GetEventListUseCase(repository)
    private val deleteEventUseCase = DeleteEventUseCase(repository)
    private val editEventUseCase = EditEventUseCase(repository)

    val eventList = getEventListUseCase.getEventList()

    fun deleteItem(item: Event){
        deleteEventUseCase.deleteEvent(item)
    }

    fun editItem(newItem: Event){
        editEventUseCase.editEvent(newItem)
    }

}