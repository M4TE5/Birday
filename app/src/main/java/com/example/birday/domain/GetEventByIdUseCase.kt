package com.example.birday.domain

import kotlinx.coroutines.flow.Flow

class GetEventByIdUseCase (private val eventListRepository: EventListRepository) {
    suspend fun getEventById(id: Int): Flow<Event?> {
        return eventListRepository.getEventById(id)
    }
}