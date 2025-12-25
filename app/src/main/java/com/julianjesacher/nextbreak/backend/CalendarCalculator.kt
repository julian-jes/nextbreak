package com.julianjesacher.nextbreak.backend

import com.julianjesacher.nextbreak.model.Calendar
import java.time.LocalDate

object CalendarCalculator {
    fun isOffDay(calendar: Calendar): Boolean {
        return !calendar.calendar[currentDayIndex(calendar)].isSchoolDay
    }

    fun daysUntilNextDayOff(calendar: Calendar): Int {
        val startIndex = currentDayIndex(calendar)
        var days = 0
        for(i in startIndex..< calendar.calendar.size) {
            if (!calendar.calendar[i].isSchoolDay) {
                break
            }
            days++
        }
        return days
    }

    fun daysUntilHolidays(calendar: Calendar): Int {
        val startIndex = currentDayIndex(calendar)
        val holidayStart = nextHolidayStart(calendar)
        var days = 0

        for(i in startIndex..< calendar.calendar.size) {
            if(calendar.calendar[i].date == holidayStart) {
                break
            }
            if(!calendar.calendar[i].isSchoolDay) {
                continue
            }
            days++
        }

        return days
    }

    fun schoolDaysLeft(calendar: Calendar): Int {
        val startIndex = currentDayIndex(calendar)
        val summerBreakStart = summerBreakStart(calendar)
        var days = 0

        for(i in startIndex..< calendar.calendar.size) {
            if(calendar.calendar[i].date == summerBreakStart) {
                break
            }
            if(!calendar.calendar[i].isSchoolDay) {
                continue
            }
            days++
        }

        return days
    }

    fun schoolYearProgress(calendar: Calendar): Float {
        var totalSchoolDays = 0
        for (i in calendar.calendar) {
            if(i.isSchoolDay) {
                totalSchoolDays++
            }
        }
        return 1 - schoolDaysLeft(calendar).toFloat() / totalSchoolDays
    }

    private fun nextHolidayStart(calendar: Calendar): String {
        val currentDate = LocalDate.parse("2025-12-22")//LocalDate.now()

        if (currentDate.isBefore(LocalDate.parse(calendar.autumnBreakStart))) {
            return calendar.autumnBreakStart
        }
        if (currentDate.isBefore(LocalDate.parse(calendar.winterBreakStart))) {
            return calendar.winterBreakStart
        }
        if (currentDate.isBefore(LocalDate.parse(calendar.carnivalBreakStart))) {
            return calendar.carnivalBreakStart
        }
        if (currentDate.isBefore(LocalDate.parse(calendar.easterBreakStart))) {
            return calendar.easterBreakStart
        }

        return summerBreakStart(calendar)
    }

    fun holidayName(calendar: Calendar): String {
        val holidayStart = nextHolidayStart(calendar)
        return when(holidayStart) {
            calendar.autumnBreakStart -> "autumn"
            calendar.winterBreakStart -> "winter"
            calendar.carnivalBreakStart -> "carnival"
            calendar.easterBreakStart -> "easter"
            else -> "summer"
        }
    }

    private fun summerBreakStart(calendar: Calendar): String {
        val lastSchoolDay = calendar.calendar[calendar.calendar.size - 1].date
        return LocalDate.parse(lastSchoolDay).plusDays(1).toString()
    }

    private fun currentDayIndex(calendar: Calendar): Int {
        val currentDate = "2025-12-22"//LocalDate.now().toString()
        return calendar.calendar.indexOfFirst {
            it.date == currentDate
        }
    }
}