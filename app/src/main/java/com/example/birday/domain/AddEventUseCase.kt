package com.example.birday.domain

class AddEventUseCase (private val eventListRepository: EventListRepository) {
    fun addEvent(event: Event){
        eventListRepository.addEvent(event)
    }
}