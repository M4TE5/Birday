package com.example.birday.domain

class GetEventByIdUseCase (private val eventListRepository: EventListRepository) {
    fun getEventById(id: Int): Event?{
        return eventListRepository.getEventById(id)
    }
}