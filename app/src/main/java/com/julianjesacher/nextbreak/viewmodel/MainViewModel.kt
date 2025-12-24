package com.julianjesacher.nextbreak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianjesacher.nextbreak.backend.CalendarCalculator
import com.julianjesacher.nextbreak.backend.CalendarRepository
import com.julianjesacher.nextbreak.backend.CheckVersionResult
import com.julianjesacher.nextbreak.backend.DownloadCalendarResult
import com.julianjesacher.nextbreak.backend.FileManager
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

const val TAG = "CustomTestLogs"

class MainViewModel : ViewModel(){
    private var _daysUntilHolidays = MutableStateFlow(-1)
    val daysUntilHolidaysDisplay: StateFlow<String> = _daysUntilHolidays
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _nextDayOff = MutableStateFlow(-1)
    val nextDayOffDisplay: StateFlow<String> = _nextDayOff
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _schoolDaysLeft = MutableStateFlow(-1)
    val schoolDaysLeftDisplay: StateFlow<String> = _schoolDaysLeft
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _schoolYearProgress = MutableStateFlow(0f)
    val schoolYearProgressDisplay = _schoolYearProgress.asStateFlow()

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {

            val calendar = CalendarRepository.getLocalCalendar()
            if(calendar != null) {
                Log.d(TAG, "Loaded local calendar successfully: $calendar")
                setCalendarUI(calendar)
            }

            when (val result = VersionRepository.checkVersion()) {
                CheckVersionResult.Error -> {
                    Log.e(TAG, "Error checking version, error happened!")
                    return@launch
                }
                CheckVersionResult.NoInternet -> {
                    Log.e(TAG, "Error checking version, no internet!")
                    return@launch
                }
                is CheckVersionResult.Success -> {
                    Log.d(TAG, "Checked version successfully, new version available: ${result.updateAvailable}")
                    if(result.updateAvailable) {
                        when (val result = CalendarRepository.downloadCalendar()) {
                            DownloadCalendarResult.Error -> {
                                Log.e(TAG, "Error downloading calendar, error happened!")
                                return@launch
                            }
                            DownloadCalendarResult.NoInternet -> {
                                Log.e(TAG, "Error downloading calendar no internet!")
                                return@launch
                            }
                            is DownloadCalendarResult.Success -> {
                                Log.d(TAG, "Calendar download successful, calendar: ${result.calendar}")
                                VersionRepository.saveVersionLocally()
                                setCalendarUI(result.calendar)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setCalendarUI(calendar: Calendar) {
        if(CalendarCalculator.isOffDay(calendar)) {
            return
        }
        _nextDayOff.value = CalendarCalculator.daysUntilNextDayOff(calendar)
        _daysUntilHolidays.value = CalendarCalculator.daysUntilHolidays(calendar)
        _schoolDaysLeft.value = CalendarCalculator.schoolDaysLeft(calendar)
        _schoolYearProgress.value = CalendarCalculator.schoolYearProgress(calendar)
    }
}