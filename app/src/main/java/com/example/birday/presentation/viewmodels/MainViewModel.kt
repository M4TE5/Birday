package com.example.birday.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.GetEventListUseCase
import com.example.birday.domain.GetFirstEventUseCase
import com.example.birday.domain.GetListSizeUseCase

class MainViewModel: ViewModel() {
    private val repository = EventListRepositoryImpl

    private val getEventListUseCase = GetEventListUseCase(repository)
    private val getFirstEventUseCase = GetFirstEventUseCase(repository)
    private val getListSizeUseCase = GetListSizeUseCase(repository)

    val eventList = getEventListUseCase.getEventList()
    val firstEvent = getFirstEventUseCase.getFirstEvent()

    fun getListSize(): Int{
        return getListSizeUseCase.getListSize()
    }
}