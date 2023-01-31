package com.example.birday.presentation

import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.GetEventListUseCase

class MainViewModel: ViewModel() {
    private val repository = EventListRepositoryImpl

    private val getEventListUseCase = GetEventListUseCase(repository)

    val eventList = getEventListUseCase.getEventList()
}