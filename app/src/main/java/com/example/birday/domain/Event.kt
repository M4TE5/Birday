package com.example.birday.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

data class Event(
    val firstName: String,
    val lastName: String,
    val date: LocalDate,
    val showDateTag: Boolean = false,
    var favorite: Boolean = false,
    var id: Int = UNDEFINED_ID
) {


    companion object {
        const val UNDEFINED_ID = -1
        const val DATE_FORMAT = "MMMM d, yyyy"

        @RequiresApi(Build.VERSION_CODES.O)
        fun daysLeft(date: LocalDate): Int {
            val currentDate = LocalDate.now()
            var newDate = LocalDate.of(currentDate.year, date.month, date.dayOfMonth)
            if (newDate < currentDate) newDate = newDate.plusYears(1)
            return newDate.until(currentDate, ChronoUnit.DAYS).toInt()
        }
    }
}
