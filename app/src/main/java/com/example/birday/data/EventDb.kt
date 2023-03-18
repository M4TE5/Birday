package com.example.birday.data

import android.content.Context
import androidx.room.*

@Database(entities = [EventDbEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class EventDb : RoomDatabase() {
    abstract fun getDao(): Dao
}