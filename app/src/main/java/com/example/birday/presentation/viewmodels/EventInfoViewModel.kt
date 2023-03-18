package com.example.birday.presentation.viewmodels

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.DeleteEventUseCase
import com.example.birday.domain.Event
import com.example.birday.domain.GetEventByIdUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period

class EventInfoViewModel(private val repository: EventListRepositoryImpl) : ViewModel() {


    private val getEventUseCase = GetEventByIdUseCase(repository)
    private val deleteEventUseCase = DeleteEventUseCase(repository)

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventById(id: Int) {
        viewModelScope.launch{
            getEventUseCase.getEventById(id).collect(){
                _event.value = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteEvent(event: Event?) {
        viewModelScope.launch {
            if (event != null) {
                deleteEventUseCase.deleteEvent(event)
            }
        }
    }

    private fun finishWork() {
        //TODO: finish work using _shouldCloseScreen: MutableLiveData<Unit>
    }
}