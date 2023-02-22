package com.example.birday.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 1)
abstract class EventDb : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        fun getDb(context: Context): EventDb {
            return Room.databaseBuilder(
                context.applicationContext,
                EventDb::class.java,
                "events.db"
            ).build()
        }
    }
}