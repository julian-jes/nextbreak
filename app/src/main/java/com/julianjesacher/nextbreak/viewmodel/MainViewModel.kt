package com.julianjesacher.nextbreak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianjesacher.nextbreak.backend.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import kotlin.system.measureTimeMillis

const val TAG = "Main View Model"

class MainViewModel : ViewModel(){
    private var _daysUntilHolidays = MutableStateFlow(7)
    val daysUntilHolidaysDisplay: StateFlow<String> = _daysUntilHolidays
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, "–")

    private var _nextDayOff = MutableStateFlow(3)
    val nextDayOffDisplay: StateFlow<String> = _nextDayOff
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, "–")

    private var _schoolDaysLeft = MutableStateFlow(70)
    val schoolDaysLeftDisplay: StateFlow<String> = _schoolDaysLeft
        .map { if (it == -1) "–" else it.toString() }
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, "–")

    private var _schoolYearProgress = MutableStateFlow(0.7f)
    val schoolYearProgressDisplay = _schoolYearProgress.asStateFlow()

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val response = try {
                    RetrofitInstance.api.getData()
                } catch (e: IOException) {
                    Log.e(TAG, "IOException: $e")
                    return@launch
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException: $e")
                    return@launch
                }

                if(response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Response successful: ${response.body()}")
                } else {
                    Log.e(TAG,  "Response was not successful")
                }
            }
            Log.d(TAG, "Duration $time ms")
        }
    }
}