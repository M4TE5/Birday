package com.example.birday.domain

data class Event(
    val name: String,
    val date: String, //Пока так
    val showDateTag: Boolean = false,
    var id :Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
