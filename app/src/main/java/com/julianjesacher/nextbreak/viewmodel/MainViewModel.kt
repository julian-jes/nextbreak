package com.julianjesacher.nextbreak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianjesacher.nextbreak.backend.CheckVersionResult
import com.julianjesacher.nextbreak.backend.VersionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val TAG = "Main View Model"

class MainViewModel : ViewModel(){
    private var _daysUntilHolidays = MutableStateFlow(7)
    val daysUntilHolidaysDisplay: StateFlow<String> = _daysUntilHolidays
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _nextDayOff = MutableStateFlow(3)
    val nextDayOffDisplay: StateFlow<String> = _nextDayOff
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _schoolDaysLeft = MutableStateFlow(70)
    val schoolDaysLeftDisplay: StateFlow<String> = _schoolDaysLeft
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "–")

    private var _schoolYearProgress = MutableStateFlow(0.7f)
    val schoolYearProgressDisplay = _schoolYearProgress.asStateFlow()

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = VersionRepository.checkVersion()) {
                CheckVersionResult.Error -> {
                    Log.e(TAG, "Error checking version, error happened!")
                }
                CheckVersionResult.NoInternet -> {
                    Log.e(TAG, "Error checking version, no internet!")
                }
                is CheckVersionResult.Success -> {
                    Log.d(TAG, "Checked version successfully, new version available: ${result.updateAvailable}")
                    if(result.updateAvailable) {
                        VersionRepository.saveVersionLocally()
                    }
                }
            }
        }
    }
}