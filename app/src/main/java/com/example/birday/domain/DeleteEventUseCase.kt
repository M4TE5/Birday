package com.example.birday.domain

class DeleteEventUseCase (private val eventListRepository: EventListRepository) {
    fun deleteEvent(event: Event){
        eventListRepository.deleteEvent(event)
    }
}