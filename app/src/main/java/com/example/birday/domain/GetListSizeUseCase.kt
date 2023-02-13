package com.example.birday.domain

class GetListSizeUseCase (private val eventListRepository: EventListRepository) {

    fun getListSize(): Int {
        return eventListRepository.getListSize()
    }
}