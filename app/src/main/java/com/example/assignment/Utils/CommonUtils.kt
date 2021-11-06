package com.example.assignment.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object CommonUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimeFromString(time: String): LocalDate {
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        return LocalDate.parse(time, formatter)
    }

    fun getFormattedMonth(month: String): String {
        return month[0] + month.substring(1, 3).toLowerCase()
    }

    fun getFormattedDay(day: Int): String {
        return if (day < 9) return "0${day}" else day.toString()
    }
}