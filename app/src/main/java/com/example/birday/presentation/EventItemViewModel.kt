package com.example.birday.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birday.data.EventListRepositoryImpl
import com.example.birday.domain.AddEventUseCase
import com.example.birday.domain.EditEventUseCase
import com.example.birday.domain.Event
import com.example.birday.domain.GetEventByIdUseCase
import kotlin.concurrent.fixedRateTimer

class EventItemViewModel: ViewModel() {
    private val repository = EventListRepositoryImpl

    private val addEventUseCase = AddEventUseCase(repository)
    private val editEventUseCase = EditEventUseCase(repository)
    private val getEventUseCase = GetEventByIdUseCase(repository)

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    fun addItem(firstName: String, lastName: String, date: String){
        if (validateInput(firstName, lastName)){
            val event = Event(firstName, lastName, date)
            addEventUseCase.addEvent(event)
        }
    }

    private fun parseName(name: String?): String{
        return name?.trim() ?: ""
    }

    private fun validateInput(inputFirstName: String, inputLastName: String): Boolean{
        val firstName = parseName(inputFirstName)
        val lastName = parseName(inputLastName)
        if (firstName.isBlank()){
            //TODO: _errorInputFirstName.value = true
            return false
        }
        if (lastName.isBlank()){
            //TODO: _errorInputLastName.value = true
            return false
        }
        return true
    }

    fun editItem(firstName: String, lastName: String, date: String){
        if (validateInput(firstName, lastName)){
            _event.value?.let {
                val event = it.copy(firstName = firstName, lastName = lastName, date = date)
                editEventUseCase.editEvent(event)
            }
        }
    }

    fun getItemById(id: Int){
        val item = getEventUseCase.getEventById(id)!!
        _event.value = item
    }
}