package com.example.birday.domain

import kotlinx.coroutines.flow.Flow

interface EventListRepository {

    suspend fun addEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun editEvent(event: Event)

    suspend fun getEventById(id: Int): Flow<Event?>

    suspend fun getEventList(): Flow<List<Event>>
}