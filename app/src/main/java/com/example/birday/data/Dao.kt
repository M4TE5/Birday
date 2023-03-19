package com.example.birday.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertItem(event: EventDbEntity)

    @Delete
    fun deleteItem(event:EventDbEntity)

    @Update
    fun updateItem(event: EventDbEntity)

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getItemById(eventId: Int): Flow<EventDbEntity?>

    @Query("SELECT * FROM events")
    fun getAllItems(): Flow<List<EventDbEntity>>

}