package com.julianjesacher.nextbreak.backend

import com.julianjesacher.nextbreak.model.Calendar
import java.time.LocalDate

object CalendarCalculator {
    fun daysUntilNextDayOff(calendar: Calendar): Int {
        val startIndex = currentDayIndex(calendar)
        var days = 0
        for(i in startIndex..calendar.calendar.size) {
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

        for(i in startIndex..calendar.calendar.size) {
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

    private fun nextHolidayStart(calendar: Calendar): String {
        val currentDate = LocalDate.now()

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

        val summerStartString = calendar.calendar[calendar.calendar.size - 1].date
        return LocalDate.parse(summerStartString).plusDays(1).toString()
    }

    private fun currentDayIndex(calendar: Calendar): Int {
        val currentDate = LocalDate.now().toString()
        return calendar.calendar.indexOfFirst {
            it.date == currentDate
        }
    }
}