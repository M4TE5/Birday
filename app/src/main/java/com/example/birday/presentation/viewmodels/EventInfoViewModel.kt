package com.example.birday.presentation.viewmodels

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.DeleteEventUseCase
import com.example.birday.domain.Event
import com.example.birday.domain.GetEventByIdUseCase
import java.time.LocalDate
import java.time.Period

class EventInfoViewModel : ViewModel() {

    private val repository = EventListRepositoryImpl

    private val getEventUseCase = GetEventByIdUseCase(repository)
    private val deleteEventUseCase = DeleteEventUseCase(repository)

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    fun getEventById(id: Int) {
        val item = getEventUseCase.getEventById(id)
        _event.value = item
    }

    fun deleteEvent(event: Event) {
        deleteEventUseCase.deleteEvent(event)
        finishWork()
    }

    private fun finishWork() {
        //TODO: finish work using _shouldCloseScreen: MutableLiveData<Unit>
    }
}