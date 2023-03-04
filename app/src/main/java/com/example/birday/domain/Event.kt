package com.example.birday.domain

import android.icu.util.LocaleData
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
    var showDateTag: Boolean = false,
    var favorite: Boolean = false,
    var notes: String = "",
    var id: Int = UNDEFINED_ID
) {

    fun getZodiacSign(): String {
        if (dateIsInBounds(21, 3, 19, 4)) return "Aries"
        if (dateIsInBounds(20, 4, 20, 5)) return "Taurus"
        if (dateIsInBounds(21, 5, 20, 6)) return "Gemini"
        if (dateIsInBounds(21, 6, 22, 7)) return "Cancer"
        if (dateIsInBounds(23, 7, 22, 8)) return "Leo"
        if (dateIsInBounds(23, 8, 22, 9)) return "Virgo"
        if (dateIsInBounds(23, 9, 22, 10)) return "Libra"
        if (dateIsInBounds(23, 10, 21, 11)) return "Scorpio"
        if (dateIsInBounds(22, 11, 21, 12)) return "Sagittarius"
        if (dateIsInBounds(22, 12, 19, 1)) return "Capricorn"
        if (dateIsInBounds(20, 1, 18, 2)) return "Aquarius"
        if (dateIsInBounds(19, 2, 20, 3)) return "Pisces"
        return "Unknown zodiac sign"
    }

    private fun dateIsInBounds(day1: Int, month1: Int, day2: Int, month2: Int): Boolean {
        var currentYear = LocalDate.now().year
        val leftBound = LocalDate.of(currentYear, month1, day1)
        if (month2 < month1) currentYear++
        val date = LocalDate.of(currentYear, date.month, date.dayOfMonth)
        val rightBound = LocalDate.of(currentYear, month2, day2)
        return (!date.isBefore(leftBound) && !date.isAfter(rightBound))
    }

    fun getChineseSign(): String {
        return when (date.year % 12) {
            0 -> "Monkey"
            1 -> "Rooster"
            2 -> "Dog"
            3 -> "Pig"
            4 -> "Rat"
            5 -> "Ox"
            6 -> "Tiger"
            7 -> "Rabbit"
            8 -> "Dragon"
            9 -> "Snake"
            10 -> "Horse"
            11 -> "Sheep"
            else -> "Unknown Chinese sign"
        }
    }

    fun getAge(): Int {
        return (Period.between(date, LocalDate.now())).years
    }

    fun daysLeft(): Int {
        val currentDate = LocalDate.now()
        var newDate = LocalDate.of(currentDate.year, date.month, date.dayOfMonth)
        if (newDate < currentDate) newDate = newDate.plusYears(1)
        return newDate.until(currentDate, ChronoUnit.DAYS).toInt()
    }

    fun getNextCelebrationDate(): LocalDate {
        val year = LocalDate.now().year
        val date = LocalDate.of(year, date.month, date.dayOfMonth)
        return LocalDate.of(
            if (!date.isBefore(LocalDate.now())) year
            else year + 1,
            date.month,
            date.dayOfMonth
        )
    }

    fun getDayName(): String = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        const val UNDEFINED_ID = -1
        const val DATE_FORMAT = "MMMM d, yyyy"
    }
}
