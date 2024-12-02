package ug.global.ecap_code.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Helper {
    fun calculateAgeWithSimpleDateFormat(birthDateStr: String, format: String = "dd/MM/yyyy"): String {
        try {
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            val birthDate: Date = formatter.parse(birthDateStr) ?: return "Age:" // handle parsing error

            val today = Date()
            val diffInMs = today.time - birthDate.time
            val ageYears = (diffInMs / (1000L * 60 * 60 * 24 * 365)).toInt()

            // Check if birthday has happened this year
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val birthCalendar = Calendar.getInstance()
            birthCalendar.time = birthDate
            val birthYear = birthCalendar.get(Calendar.YEAR)
            if (currentYear > birthYear && birthCalendar.get(Calendar.DAY_OF_YEAR) > birthDate.time) {
                ageYears - 1
            }
            return "$ageYears"

        } catch (ex: Exception) {
            return "0"
        }
    }

}