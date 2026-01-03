package com.julianjesacher.nextbreak.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.julianjesacher.nextbreak.ui.components.LoadingOverlay
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

@Composable
fun NextBreakApp(viewModel: MainViewModel) {

    val isSchoolDay by viewModel.isSchoolDay.collectAsState()
    val isInfoDialogOpen by viewModel.isInfoDialogOpen.collectAsState()
    val showLoadingOverlay by viewModel.showLoadingOverlay.collectAsState()
    val showNoDataScreen by viewModel.showNoDataScreen.collectAsState()

    if(showNoDataScreen) {
        NoDataScreen(viewModel)
    }
    else {
        if(isSchoolDay) {
            MainScreen(viewModel)
        } else {
            DayOffScreen(viewModel)
        }
    }

    if(isInfoDialogOpen) {
        InfoDialog(viewModel)
    }

    LoadingOverlay(showLoadingOverlay)
}