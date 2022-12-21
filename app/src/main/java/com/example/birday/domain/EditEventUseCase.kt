package com.example.birday.domain

class EditEventUseCase (private val eventListRepository: EventListRepository) {
    fun editEvent(event: Event){
        eventListRepository.editEvent(event)
    }
}