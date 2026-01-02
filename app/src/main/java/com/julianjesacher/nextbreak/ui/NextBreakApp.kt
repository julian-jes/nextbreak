package com.julianjesacher.nextbreak.ui

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.julianjesacher.nextbreak.ui.components.LoadingOverlay
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

@Composable
fun NextBreakApp(viewModel: MainViewModel) {

    val isSchoolDay by viewModel.isSchoolDay.collectAsState()
    val isInfoDialogOpen by viewModel.isInfoDialogOpen.collectAsState()
    val showLoadingOverlay by viewModel.showLoadingOverlay.collectAsState()

    if(isSchoolDay) {
        MainScreen(viewModel)
    } else {
        DayOffScreen(viewModel)
    }
    if(isInfoDialogOpen) {
        InfoDialog(viewModel)
    }

    LoadingOverlay(showLoadingOverlay)
}