package com.example.birday.domain

import androidx.lifecycle.LiveData

class GetEventListUseCase (private val eventListRepository: EventListRepository){
    fun getEventList(): LiveData<List<Event>>{
        return eventListRepository.getEventList()
    }
}