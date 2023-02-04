package com.example.birday.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
@RequiresApi(Build.VERSION_CODES.O)
class Event(
    val firstName: String,
    val lastName: String,
    val date: LocalDate,
    val showDateTag: Boolean = false,
    var favorite: Boolean = false,
    var id: Int = UNDEFINED_ID
) {

    fun getAge(): Int{
        return (Period.between(date, LocalDate.now())).years
    }
    fun daysLeft(): Int{
        val currentDate = LocalDate.now()
        var newDate = LocalDate.of(currentDate.year, date.month, date.dayOfMonth)
        if (newDate < currentDate) newDate = newDate.plusYears(1)
        return newDate.until(currentDate, ChronoUnit.DAYS).toInt()
    }
    fun getNextCelebrationDate(): LocalDate = LocalDate.of(
        LocalDate.now().year,
        date.month,
        date.dayOfMonth
    )
    fun getDayName(): String = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    companion object {
        const val UNDEFINED_ID = -1
        const val DATE_FORMAT = "MMMM d, yyyy"
    }
}
