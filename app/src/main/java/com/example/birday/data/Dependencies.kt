package com.example.birday.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.birday.domain.EventListRepository

@RequiresApi(Build.VERSION_CODES.O)
object Dependencies {
    private lateinit var applicationContext: Context

    fun init(context: Context){
        applicationContext = context
    }

    private val database: EventDb by lazy {
        Room.databaseBuilder(applicationContext, EventDb::class.java,"database.db")
            .build()
    }

    val eventListRepository: EventListRepositoryImpl by lazy { EventListRepositoryImpl(database.getDao()) }
}