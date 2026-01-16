package com.julianjesacher.nextbreak.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.julianjesacher.nextbreak.ui.components.LoadingOverlay
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

@Composable
fun NextBreakApp(viewModel: MainViewModel) {

    val isSummerHoliday by viewModel.isSummerHoliday.collectAsState()
    val isAboutDialogOpen by viewModel.isAboutDialogOpen.collectAsState()
    val showLoadingOverlay by viewModel.showLoadingOverlay.collectAsState()
    val showNoDataScreen by viewModel.showNoDataScreen.collectAsState()

    if(showNoDataScreen) {
        NoDataScreen(viewModel)
    }
    else {
        if(isSummerHoliday) {
            SummerHolidayScreen(viewModel)
        } else {
            MainScreen(viewModel)
        }
    }

    if(isAboutDialogOpen) {
        AboutDialog(viewModel)
    }

    LoadingOverlay(showLoadingOverlay)
}