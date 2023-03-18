package com.example.birday.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birday.data.Dao
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class EventListViewModel(private val repository: EventListRepositoryImpl): ViewModel() {

    private val editEventUseCase = EditEventUseCase(repository)
    private val getEventListUseCase = GetEventListUseCase(repository)

    private val _eventList = MutableLiveData<List<Event>>()
    val eventList: LiveData<List<Event>>
        get() = _eventList
    init{
        viewModelScope.launch {
            getEventListUseCase.getEventList().collect(){ list->
                _eventList.value = list
            }
        }
    }

    fun editEvent(event: Event){
        viewModelScope.launch {
            editEventUseCase.editEvent(event)
        }
    }
}