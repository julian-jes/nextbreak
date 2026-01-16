package com.julianjesacher.nextbreak.domain

import com.julianjesacher.nextbreak.data.VersionRepository
import com.julianjesacher.nextbreak.models.Calendar
import java.time.LocalDate

object CalendarCalculator {
    fun isOffDay(calendar: Calendar): Boolean {
        val index = currentDayIndex(calendar)
        return index == -1 || !calendar.calendar[index].isSchoolDay
    }

    fun isHoliday(calendar: Calendar): Boolean {
        val holidayIndex = nextHolidayIndex(calendar) - 1
        val currentDayIndex = currentDayIndex(calendar)

        if(holidayIndex == -1) {
            return false
        }

        val holidayStart = holidayStartOfIndex(calendar, holidayIndex)
        val startIndex = calendar.calendar.indexOfFirst {
            it.date == holidayStart
        }

        for (i in startIndex..currentDayIndex) {
            if(calendar.calendar[i].isSchoolDay) {
                return false
            }
        }

        return true
    }

    suspend fun isSummerHoliday(calendar: Calendar): Boolean {
        val version = VersionRepository.getLocalVersion() ?: return false
        val localDate = LocalDate.now()

        return if(version.year >= localDate.year) {
            localDate.isBefore(LocalDate.parse(calendar.calendar[0].date))
        } else {
            localDate.isAfter(LocalDate.parse(calendar.calendar[calendar.calendar.size - 1].date))
        }
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

    fun daysUntilHolidays(calendar: Calendar, holidayIndex: Int): Int {
        val startIndex = currentDayIndex(calendar)
        val holidayStart = holidayStartOfIndex(calendar, holidayIndex)
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

    private fun holidayStartOfIndex(calendar: Calendar, holidayIndex: Int): String {
        return when(holidayIndex) {
            0 -> calendar.autumnBreakStart
            1 -> calendar.winterBreakStart
            2 -> calendar.carnivalBreakStart
            3 -> calendar.easterBreakStart
            else -> summerBreakStart(calendar)
        }
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

    fun nextHolidayIndex(calendar: Calendar): Int {
        val currentDate = LocalDate.now()

        if (currentDate.isBefore(LocalDate.parse(calendar.autumnBreakStart))) {
            return 0
        }
        if (currentDate.isBefore(LocalDate.parse(calendar.winterBreakStart))) {
            return 1
        }
        if (currentDate.isBefore(LocalDate.parse(calendar.carnivalBreakStart))) {
            return 2
        }
        if (currentDate.isBefore(LocalDate.parse(calendar.easterBreakStart))) {
            return 3
        }

        return 4
    }

    suspend fun isCalendarToOld(): Boolean {
        val currentDate = LocalDate.now()
        val version = VersionRepository.getLocalVersion() ?: return false

        return currentDate.monthValue >= 9 && currentDate.year > version.year
    }

    fun holidayName(holidayIndex: Int): String {
        return when(holidayIndex) {
            0 -> "autumn"
            1 -> "winter"
            2 -> "carnival"
            3 -> "easter"
            else -> "summer"
        }
    }

    private fun summerBreakStart(calendar: Calendar): String {
        val lastSchoolDay = calendar.calendar[calendar.calendar.size - 1].date
        return LocalDate.parse(lastSchoolDay).plusDays(1).toString()
    }

    private fun currentDayIndex(calendar: Calendar): Int {
        val currentDate = LocalDate.now().toString()
        return calendar.calendar.indexOfFirst {
            it.date == currentDate
        }
    }
}