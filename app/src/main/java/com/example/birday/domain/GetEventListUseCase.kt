package com.example.birday.domain

import kotlinx.coroutines.flow.Flow

class GetEventListUseCase (private val eventListRepository: EventListRepository){
    suspend fun getEventList(): Flow<List<Event>> {
        return eventListRepository.getEventList()
    }
}