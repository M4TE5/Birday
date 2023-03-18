package com.example.birday.domain

class DeleteEventUseCase (private val eventListRepository: EventListRepository) {
    suspend fun deleteEvent(event: Event){
        eventListRepository.deleteEvent(event)
    }
}