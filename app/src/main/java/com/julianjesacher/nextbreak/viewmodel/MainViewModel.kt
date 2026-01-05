package com.julianjesacher.nextbreak.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.julianjesacher.nextbreak.domain.CalendarCalculator
import com.julianjesacher.nextbreak.data.CalendarRepository
import com.julianjesacher.nextbreak.data.CheckVersionResult
import com.julianjesacher.nextbreak.data.DownloadCalendarResult
import com.julianjesacher.nextbreak.data.VersionRepository
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.models.Calendar
import com.julianjesacher.nextbreak.utils.AppVersionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val appContext = getApplication<Application>()

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

    private var _isSchoolDay = MutableStateFlow(true)
    val isSchoolDay = _isSchoolDay.asStateFlow()

    private var _isAboutDialogOpen = MutableStateFlow(false)
    val isAboutDialogOpen = _isAboutDialogOpen.asStateFlow()

    private var _appVersionText = MutableStateFlow("")
    val appVersionText = _appVersionText.asStateFlow()

    private var _showLoadingOverlay = MutableStateFlow(false)
    val showLoadingOverlay = _showLoadingOverlay.asStateFlow()

    private var _refreshButtonText: MutableStateFlow<String?> = MutableStateFlow(null)
    val refreshButtonText = _refreshButtonText.asStateFlow()

    private var _showNoDataScreen = MutableStateFlow(false)
    val showNoDataScreen = _showNoDataScreen.asStateFlow()

    fun setAboutDialog(isOpen: Boolean) {
        _isAboutDialogOpen.value = isOpen
    }

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            updateVersionInfo()
            val loadedLocalCalendar = loadLocalCalendar()
            loadOnlineData(!loadedLocalCalendar)
        }
    }

    fun retryLoadingOnlineData() {
        viewModelScope.launch(Dispatchers.IO) {
            loadOnlineData(true)
        }
    }

    fun generateFeedbackUrl(): String {
        return "${AppConstants.FEEDBACK_BASE_URL}${AppConstants.FEEDBACK_ENTRY}${Uri.encode(_appVersionText.value)}"
    }

    private suspend fun loadOnlineData(loadingOverlayAtStart: Boolean) {
        if(loadingOverlayAtStart) _showLoadingOverlay.value = true

        val result = checkVersion() ?: return

        if(result.updateAvailable) {
            _showLoadingOverlay.value = true
            val calendar = downloadCalendar() ?: return

            VersionRepository.saveVersionLocally()
            setCalendarUI(calendar)
            updateVersionInfo()
        }

        _showLoadingOverlay.value = false
    }

    private suspend fun updateVersionInfo() {
        val appVersion = AppVersionUtils.getAppVersion(appContext)
        val dataVersion = VersionRepository.getLocalVersion()

        var dataVersionText = "-"
        if(dataVersion != null) {
            dataVersionText = "${dataVersion.year}.${dataVersion.hotfix}"
        }

        _appVersionText.value = "v.$appVersion ($dataVersionText)"
    }

    private suspend fun loadLocalCalendar(): Boolean {
        _showLoadingOverlay.value = true
        val calendar = CalendarRepository.loadLocalCalendar()

        if(calendar != null) {
            setCalendarUI(calendar)
            updateVersionInfo()
            _showLoadingOverlay.value = false
            return true
        }

        _showNoDataScreen.value = true
        _showLoadingOverlay.value = false
        return false
    }

    private suspend fun downloadCalendar(): Calendar? {
        return when (val result = CalendarRepository.downloadCalendar(appContext)) {
            DownloadCalendarResult.Error -> {
                _refreshButtonText.value = "Network Error"
                _showLoadingOverlay.value = false
                Log.e(AppConstants.LOG_TAG, "Error downloading calendar, error happened!")
                null
            }
            DownloadCalendarResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
                _showLoadingOverlay.value = false
                Log.e(AppConstants.LOG_TAG, "Error downloading calendar no internet!")
                null
            }
            is DownloadCalendarResult.Success -> {
                _refreshButtonText.value = null
                result.calendar
            }
        }
    }

    private suspend fun checkVersion(): CheckVersionResult.Success? {
        return when (val result = VersionRepository.checkVersion(appContext)) {
            CheckVersionResult.Error -> {
                _refreshButtonText.value = "Network Error"
                _showLoadingOverlay.value = false
                Log.e(AppConstants.LOG_TAG, "Error checking version, error happened!")
                null
            }
            CheckVersionResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
                _showLoadingOverlay.value = false
                Log.e(AppConstants.LOG_TAG, "Error checking version, no internet!")
                null
            }
            is CheckVersionResult.Success -> {
                _refreshButtonText.value = null
                result
            }
        }
    }

    private fun setCalendarUI(calendar: Calendar) {

        _showNoDataScreen.value = false

        if(CalendarCalculator.isOffDay(calendar)) {
            _isSchoolDay.value = false
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
            _schoolDaysLeftText.value = "$schoolDaysLeft school days left"
        }

        _schoolYearProgress.value = CalendarCalculator.schoolYearProgress(calendar)
    }


}