package com.julianjesacher.nextbreak.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

@Composable
fun NoDataScreen(viewModel: MainViewModel) {

    val refreshButtonText by viewModel.refreshButtonText.collectAsState()
    val showUpdateButton by viewModel.showUpdateButton.collectAsState()

    BaseScreen(
        onInfoClick = {
            viewModel.setAboutDialog(true)
        },
        onRetryClick = {
            viewModel.retryLoadingOnlineData()
        },
        onUpdateClick = {
            viewModel.openReleasesUrl()
        },
        showUpdateButton = showUpdateButton,
        refreshButtonText = refreshButtonText
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 180.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No data available",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 55.sp,
                lineHeight = 60.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Unable to load data.\nPlease try again.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 30.sp,
                lineHeight = 35.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun NoDataScreenPreview() {
    NextBreakTheme {
        NoDataScreen(MainViewModel(Application()))
    }
}