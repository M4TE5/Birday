package com.example.birday.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.birday.domain.Event
import com.example.birday.domain.EventListRepository
import com.example.birday.presentation.EventListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import java.util.TreeSet
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class EventListRepositoryImpl(private val dao: Dao) : EventListRepository {

    override suspend fun addEvent(event: Event) {
        withContext(Dispatchers.IO){
            dao.insertItem(event.toEventDbEntity())
        }
    }

    override suspend fun deleteEvent(event: Event) {
        withContext(Dispatchers.IO){
            dao.deleteItem(event.toEventDbEntity())
        }
    }

    override suspend fun editEvent(event: Event) {
        withContext(Dispatchers.IO){
            dao.updateItem(event.toEventDbEntity())
        }
    }

    override suspend fun getEventById(id: Int): Flow<Event?> = dao.getItemById(id).map { it?.toEvent() }

    override suspend fun getEventList(): Flow<List<Event>> {
        return dao.getAllItems().map { list -> list.map { it.toEvent() } }
    }
}
