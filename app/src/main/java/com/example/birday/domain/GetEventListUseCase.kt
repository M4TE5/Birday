package com.example.birday.domain

class GetEventListUseCase (private val eventListRepository: EventListRepository){
    fun getEventList(): List<Event>{
        return eventListRepository.getEventList()
    }
}