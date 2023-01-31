package com.example.birday.domain

data class Event(
    val firstName: String,
    val lastName: String,
    val date: String, //Пока так
    val showDateTag: Boolean = false,
    var favorite: Boolean = false,
    var id :Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
