package com.example.birday.domain

import androidx.lifecycle.LiveData

class GetFirstEventUseCase(private val eventListRepository: EventListRepository) {
    fun getFirstEvent():  LiveData<Event>{
        return eventListRepository.getFirstEvent()
    }
}