package com.example.birday.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.Event
import com.example.birday.domain.GetEventByIdUseCase
import com.example.birday.domain.GetFirstEventUseCase
import com.example.birday.domain.GetListSizeUseCase

class BannerViewModel : ViewModel() {
    private val repository = EventListRepositoryImpl

    private val getListSizeUseCase = GetListSizeUseCase(repository)
    private val getFirstEventUseCase = GetFirstEventUseCase(repository)

    val firstEvent = getFirstEventUseCase.getFirstEvent()

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event


    fun getListSize(): Int{
        return getListSizeUseCase.getListSize()
    }

}