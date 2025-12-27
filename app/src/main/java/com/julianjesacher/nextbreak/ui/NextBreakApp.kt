package com.julianjesacher.nextbreak.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

@Composable
fun NextBreakApp(viewModel: MainViewModel) {

    //Main screen
    val daysUntilHolidays by viewModel.daysUntilHolidaysDisplay.collectAsState()
    val daysUntilHolidaysText by viewModel.daysUntilHolidaysTextDisplay.collectAsState()
    val nextDayOff by viewModel.nextDayOffTextDisplay.collectAsState()
    val schoolDaysLeft by viewModel.schoolDaysLeftTextDisplay.collectAsState()
    val schoolYearProgress by viewModel.schoolYearProgressDisplay.collectAsState()

    val isSchoolDay by viewModel.isSchoolDay.collectAsState()
    if(isSchoolDay) {
        MainScreen(daysUntilHolidays, daysUntilHolidaysText, nextDayOff, schoolDaysLeft, schoolYearProgress)
    } else {
        DayOffScreen()
    }
}