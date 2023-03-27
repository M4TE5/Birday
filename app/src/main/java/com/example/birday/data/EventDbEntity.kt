package com.example.birday.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.birday.domain.Event
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity(tableName = "events")
data class EventDbEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "firstName")
    var firstName: String,
    @ColumnInfo(name = "lastName")
    var lastName: String,
    @ColumnInfo(name = "date")
    var date:LocalDate,
    @ColumnInfo(name = "eventType")
    var eventType: String,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean,
    @ColumnInfo(name = "notes")
    var notes: String,
    @ColumnInfo(name = "imageUrl")
    var imageUri: String?
    ){

    @RequiresApi(Build.VERSION_CODES.O)
    fun toEvent(): Event = Event(
        firstName = firstName,
        lastName = lastName,
        date = date,
        eventType = eventType,
        favorite = isFavorite,
        notes = notes,
        imageUri = imageUri,
        id = id
    )
}