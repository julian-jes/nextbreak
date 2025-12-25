package com.julianjesacher.nextbreak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianjesacher.nextbreak.backend.CalendarCalculator
import com.julianjesacher.nextbreak.backend.CalendarRepository
import com.julianjesacher.nextbreak.backend.CheckVersionResult
import com.julianjesacher.nextbreak.backend.DownloadCalendarResult
import com.julianjesacher.nextbreak.backend.VersionRepository
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.model.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){
    private var _daysUntilHolidays = MutableStateFlow(-1)
    val daysUntilHolidaysDisplay: StateFlow<String> = _daysUntilHolidays
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _daysUntilHolidaysText = MutableStateFlow("")
    val daysUntilHolidaysTextDisplay = _daysUntilHolidaysText.asStateFlow()

    private var _nextDayOffText = MutableStateFlow("-")
    val nextDayOffTextDisplay = _nextDayOffText.asStateFlow()

    private var _schoolDaysLeftText = MutableStateFlow("-")
    val schoolDaysLeftTextDisplay = _schoolDaysLeftText.asStateFlow()

    private var _schoolYearProgress = MutableStateFlow(0f)
    val schoolYearProgressDisplay = _schoolYearProgress.asStateFlow()

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            loadLocalCalendar()

            val result = checkVersion() ?: return@launch
            if(result.updateAvailable) {
                val calendar = downloadCalendar() ?: return@launch

                VersionRepository.saveVersionLocally()
                setCalendarUI(calendar)
            }
        }
    }

    private suspend fun loadLocalCalendar() {
        val calendar = CalendarRepository.loadLocalCalendar()
        if(calendar != null) {
            Log.d(AppConstants.LOG_TAG, "Loaded local calendar successfully: $calendar")
            setCalendarUI(calendar)
        }
    }

    private suspend fun downloadCalendar(): Calendar? {
        return when (val result = CalendarRepository.downloadCalendar()) {
            DownloadCalendarResult.Error -> {
                Log.e(AppConstants.LOG_TAG, "Error downloading calendar, error happened!")
                null
            }
            DownloadCalendarResult.NoInternet -> {
                Log.e(AppConstants.LOG_TAG, "Error downloading calendar no internet!")
                null
            }
            is DownloadCalendarResult.Success -> result.calendar
        }
    }

    private suspend fun checkVersion(): CheckVersionResult.Success? {
        return when (val result = VersionRepository.checkVersion()) {
            CheckVersionResult.Error -> {
                Log.e(AppConstants.LOG_TAG, "Error checking version, error happened!")
                null
            }
            CheckVersionResult.NoInternet -> {
                Log.e(AppConstants.LOG_TAG, "Error checking version, no internet!")
                null
            }
            is CheckVersionResult.Success -> result
        }
    }

    private fun setCalendarUI(calendar: Calendar) {
        if(CalendarCalculator.isOffDay(calendar)) {
            return
        }

        val nextDayOff = CalendarCalculator.daysUntilNextDayOff(calendar)
        if (nextDayOff == 1) {
            _nextDayOffText.value = "Next day off in $nextDayOff day"
        }
        else {
            _nextDayOffText.value = "Next day off in $nextDayOff days"
        }
        _daysUntilHolidays.value = CalendarCalculator.daysUntilHolidays(calendar)
        var holidaysUnit = "days"
        val holidayName = CalendarCalculator.holidayName(calendar)
        if(_daysUntilHolidays.value == 1) {
            holidaysUnit = "day"
        }
        _daysUntilHolidaysText.value = "school $holidaysUnit until\n$holidayName break"

        val schoolDaysLeft = CalendarCalculator.schoolDaysLeft(calendar)
        if (schoolDaysLeft == 1) {
            _schoolDaysLeftText.value = "$schoolDaysLeft school day left"
        }
        else {
            _schoolDaysLeftText.value = "$schoolDaysLeft schools day left"
        }

        _schoolYearProgress.value = CalendarCalculator.schoolYearProgress(calendar)
    }


}