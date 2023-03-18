package com.example.birday.domain

class EditEventUseCase (private val eventListRepository: EventListRepository) {
    suspend fun editEvent(event: Event){
        eventListRepository.editEvent(event)
    }
}