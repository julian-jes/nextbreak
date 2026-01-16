package com.julianjesacher.nextbreak.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.data.CalendarRepository
import com.julianjesacher.nextbreak.data.CheckVersionResult
import com.julianjesacher.nextbreak.data.DownloadCalendarResult
import com.julianjesacher.nextbreak.data.FileManager
import com.julianjesacher.nextbreak.data.VersionRepository
import com.julianjesacher.nextbreak.domain.CalendarCalculator
import com.julianjesacher.nextbreak.models.Calendar
import com.julianjesacher.nextbreak.ui.widgets.Widget
import com.julianjesacher.nextbreak.utils.AppVersionUtils
import com.julianjesacher.nextbreak.utils.CheckUpdatesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val appContext = getApplication<Application>()

    private var _daysUntilHolidays = MutableStateFlow(List(1) {"-"})
    val daysUntilHolidays = _daysUntilHolidays.asStateFlow()

    private var _daysUntilHolidaysText = MutableStateFlow(List(1) {""})
    val daysUntilHolidaysText = _daysUntilHolidaysText.asStateFlow()

    private var _nextDayOffText = MutableStateFlow("-")
    val nextDayOffText = _nextDayOffText.asStateFlow()

    private var _schoolDaysLeftText = MutableStateFlow("-")
    val schoolDaysLeftText = _schoolDaysLeftText.asStateFlow()

    private var _schoolYearProgress = MutableStateFlow(0f)
    val schoolYearProgress = _schoolYearProgress.asStateFlow()

    private var _isSummerHoliday = MutableStateFlow(false)
    val isSummerHoliday = _isSummerHoliday.asStateFlow()

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

    private var _showUpdateButton = MutableStateFlow(false)
    val showUpdateButton = _showUpdateButton.asStateFlow()

    private var _isHoliday = MutableStateFlow(false)
    val isHoliday = _isHoliday.asStateFlow()

    private var latestReleaseUrl = ""

    fun setAboutDialog(isOpen: Boolean) {
        _isAboutDialogOpen.value = isOpen
    }

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            updateVersionInfo()
            val loadedLocalCalendar = loadLocalCalendar()
            val errorWhileLoading = loadOnlineData(!loadedLocalCalendar)

            if(!loadedLocalCalendar && errorWhileLoading) {
                _showNoDataScreen.value = true
            }

            checkForUpdates()
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

    private suspend fun loadOnlineData(loadingOverlayAtStart: Boolean): Boolean {
        if(loadingOverlayAtStart) _showLoadingOverlay.value = true

        val result = checkVersion() ?: return true

        if(result.updateAvailable) {
            _showLoadingOverlay.value = true
            val calendar = downloadCalendar() ?: return true

            VersionRepository.saveVersionLocally()
            setCalendarUI(calendar)
            updateVersionInfo()
        }

        _showLoadingOverlay.value = false
        return false
    }

    private suspend fun updateVersionInfo() {
        val appVersion = AppVersionUtils.getLocalVersion(appContext)
        val dataVersion = VersionRepository.getLocalVersion()

        var dataVersionText = "-"
        if(dataVersion != null) {
            dataVersionText = "${dataVersion.year}.${dataVersion.hotfix}"
        }

        _appVersionText.value = "v$appVersion ($dataVersionText)"
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

        _showLoadingOverlay.value = false
        return false
    }

    private suspend fun downloadCalendar(): Calendar? {
        return when (val result = CalendarRepository.downloadCalendar(appContext)) {
            DownloadCalendarResult.Error -> {
                _refreshButtonText.value = "Network Error"
                _showLoadingOverlay.value = false
                null
            }
            DownloadCalendarResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
                _showLoadingOverlay.value = false
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
                null
            }
            CheckVersionResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
                _showLoadingOverlay.value = false
                null
            }
            is CheckVersionResult.Success -> {
                _refreshButtonText.value = null
                result
            }
        }
    }

    private suspend fun setCalendarUI(calendar: Calendar) {

        Widget.updateAll(appContext)

        if(CalendarCalculator.isCalendarToOld()) {
            _showNoDataScreen.value = true
            return
        }

        _showNoDataScreen.value = false

        if(CalendarCalculator.isSummerHoliday(calendar)) {
            _isSummerHoliday.value = true
            return
        }

        when (val nextDayOff = CalendarCalculator.daysUntilNextDayOff(calendar)) {
            0 -> {
                _nextDayOffText.value = "No school today!"
            }
            1 -> {
                _nextDayOffText.value = "Next day off in $nextDayOff day"
            }
            else -> {
                _nextDayOffText.value = "Next day off in $nextDayOff days"
            }
        }


        val newDaysUntilHolidays = mutableListOf<String>()
        val newDaysUntilHolidaysText = mutableListOf<String>()
        val nextHolidayIndex = CalendarCalculator.nextHolidayIndex(calendar)

        _isHoliday.value = CalendarCalculator.isHoliday(calendar)
        if(_isHoliday.value) {
            val holidayName = CalendarCalculator.holidayName(nextHolidayIndex - 1)
            newDaysUntilHolidays.add("")
            newDaysUntilHolidaysText.add("Enjoy your $holidayName break!")
        }

        for (i in nextHolidayIndex..< 5)
        {
            newDaysUntilHolidays.add(CalendarCalculator.daysUntilHolidays(calendar, i).toString())
            var holidaysUnit = "days"
            val holidayName = CalendarCalculator.holidayName(i)
            if(newDaysUntilHolidays[newDaysUntilHolidays.size - 1].toInt() == 1) {
                holidaysUnit = "day"
            }
            newDaysUntilHolidaysText.add("school $holidaysUnit until\n$holidayName break")
        }

        _daysUntilHolidays.value = newDaysUntilHolidays
        _daysUntilHolidaysText.value = newDaysUntilHolidaysText


        val schoolDaysLeft = CalendarCalculator.schoolDaysLeft(calendar)
        if (schoolDaysLeft == 1) {
            _schoolDaysLeftText.value = "$schoolDaysLeft school day left"
        }
        else {
            _schoolDaysLeftText.value = "$schoolDaysLeft school days left"
        }

        _schoolYearProgress.value = CalendarCalculator.schoolYearProgress(calendar)
    }

    private suspend fun checkForUpdates() {
        _showLoadingOverlay.value = true
        val lastCheck = FileManager.loadFile(AppConstants.CHECK_UPDATE_INTERVAL_FILE_NAME)?.toLong()
        val currentTime = System.currentTimeMillis()

        if(lastCheck != null && (currentTime - lastCheck) < AppConstants.CHECK_UPDATE_INTERVAL) {
            _showLoadingOverlay.value = false
            return
        }

        FileManager.saveFile(AppConstants.CHECK_UPDATE_INTERVAL_FILE_NAME, currentTime.toString())

        when (val result = AppVersionUtils.checkForUpdates(appContext)) {
            CheckUpdatesResult.Error -> {
                _refreshButtonText.value = "Network Error"
                _showLoadingOverlay.value = false
            }
            CheckUpdatesResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
                _showLoadingOverlay.value = false
            }
            is CheckUpdatesResult.Success -> {
                _refreshButtonText.value = null
                _showLoadingOverlay.value = false
                _showUpdateButton.value = result.updateAvailable
                latestReleaseUrl = result.releaseUrl
            }
        }

    }

    fun openReleasesUrl() {
        val intent = Intent(Intent.ACTION_VIEW, latestReleaseUrl.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }

}