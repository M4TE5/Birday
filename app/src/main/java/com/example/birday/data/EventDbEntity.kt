package com.example.birday.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "events")
data class EventDbEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
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
    var notes: String
    )