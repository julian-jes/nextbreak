package com.julianjesacher.nextbreak.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.data.CalendarRepository
import com.julianjesacher.nextbreak.data.CheckVersionResult
import com.julianjesacher.nextbreak.data.DownloadCalendarResult
import com.julianjesacher.nextbreak.data.FileManager
import com.julianjesacher.nextbreak.data.VersionRepository
import com.julianjesacher.nextbreak.domain.CalendarCalculator
import com.julianjesacher.nextbreak.models.AppUpdateCheck
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

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var _refreshButtonText: MutableStateFlow<String?> = MutableStateFlow(null)
    val refreshButtonText = _refreshButtonText.asStateFlow()

    private var _showNoDataScreen = MutableStateFlow(false)
    val showNoDataScreen = _showNoDataScreen.asStateFlow()

    private var _showUpdateButton = MutableStateFlow(false)
    val showUpdateButton = _showUpdateButton.asStateFlow()

    private var _isHoliday = MutableStateFlow(false)
    val isHoliday = _isHoliday.asStateFlow()

    private var latestReleaseUrl = ""

    private var lastRefreshTime = 0L

    fun setAboutDialog(isOpen: Boolean) {
        _isAboutDialogOpen.value = isOpen
    }

    fun loadData(){

        if(_isRefreshing.value || (System.currentTimeMillis() - lastRefreshTime) <= 1000) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.value = true

            updateVersionInfo()
            val loadedLocalCalendar = loadLocalCalendar()
            val errorWhileLoading = loadOnlineData()
            if(!loadedLocalCalendar && errorWhileLoading) {
                _showNoDataScreen.value = true
                Widget.updateAll(appContext)
            }

            checkForUpdates()

            lastRefreshTime = System.currentTimeMillis()
            _isRefreshing.value = false
        }

    }

    fun retryLoadingOnlineData() {

        if(_isRefreshing.value || (System.currentTimeMillis() - lastRefreshTime) <= 1000) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.value = true

            loadOnlineData()

            lastRefreshTime = System.currentTimeMillis()
            _isRefreshing.value = false
        }
    }

    fun generateFeedbackUrl(): String {
        return "${AppConstants.FEEDBACK_BASE_URL}${AppConstants.FEEDBACK_ENTRY}${Uri.encode(_appVersionText.value)}"
    }

    private suspend fun loadOnlineData(): Boolean {

        val result = checkVersion() ?: return true

        if(result.updateAvailable) {
            val calendar = downloadCalendar() ?: return true

            VersionRepository.saveVersionLocally()
            setCalendarUI(calendar)
            updateVersionInfo()
        }

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
        val calendar = CalendarRepository.loadLocalCalendar()
        if(calendar != null) {
            setCalendarUI(calendar)
            updateVersionInfo()
            return true
        }

        return false
    }

    private suspend fun downloadCalendar(): Calendar? {
        return when (val result = CalendarRepository.downloadCalendar(appContext)) {
            DownloadCalendarResult.Error -> {
                _refreshButtonText.value = "Network Error"
                null
            }
            DownloadCalendarResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
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
                null
            }
            CheckVersionResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
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

        val updateCheckJson = FileManager.loadFile(AppConstants.CHECK_UPDATE_FILE_NAME)

        val localVersionString = AppVersionUtils.getLocalVersion(appContext).replace("-debug", "")
        val localVersion = localVersionString.split(".").map { it.toInt() }

        val updateCheck: AppUpdateCheck = try {
           Gson().fromJson(updateCheckJson, AppUpdateCheck::class.java)
        } catch (e: Exception) {
            AppUpdateCheck(0, localVersion, "")
        }

        _showUpdateButton.value = AppVersionUtils.isUpdate(localVersion, updateCheck.lastCheckedVersion)
        latestReleaseUrl = updateCheck.lastReleaseUrl

        val currentTime = System.currentTimeMillis()

        if((currentTime - updateCheck.lastCheck) < AppConstants.CHECK_UPDATE_INTERVAL) {
            return
        }

        when (val result = AppVersionUtils.checkForUpdates(appContext)) {
            CheckUpdatesResult.Error -> {
                _refreshButtonText.value = "Network Error"
            }
            CheckUpdatesResult.NoInternet -> {
                _refreshButtonText.value = "No Internet"
            }
            is CheckUpdatesResult.Success -> {
                _refreshButtonText.value = null
                _showUpdateButton.value = result.updateAvailable
                latestReleaseUrl = result.releaseUrl
                updateCheck.lastReleaseUrl = result.releaseUrl
                updateCheck.lastCheckedVersion = result.version
            }
        }

        updateCheck.lastCheck = System.currentTimeMillis()
        FileManager.saveFile(AppConstants.CHECK_UPDATE_FILE_NAME, Gson().toJson(updateCheck))
    }

    fun openReleasesUrl() {
        val intent = Intent(Intent.ACTION_VIEW, latestReleaseUrl.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }

}