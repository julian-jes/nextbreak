package com.julianjesacher.nextbreak.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme
import com.julianjesacher.nextbreak.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val daysUntilHolidays by viewModel.daysUntilHolidaysDisplay.collectAsState()
    val daysUntilHolidaysText by viewModel.daysUntilHolidaysTextDisplay.collectAsState()
    val nextDayOff by viewModel.nextDayOffTextDisplay.collectAsState()
    val schoolDaysLeft by viewModel.schoolDaysLeftTextDisplay.collectAsState()
    val schoolYearProgress by viewModel.schoolYearProgressDisplay.collectAsState()
    val refreshButtonText by viewModel.refreshButtonText.collectAsState()

    BaseScreen(
        onInfoClick = {
            viewModel.setInfoDialog(true)
        },
        onRetryClick = {
            viewModel.retryLoadingOnlineData()
        },
        refreshButtonText = refreshButtonText
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = 170.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = daysUntilHolidays,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = daysUntilHolidaysText,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 30.sp,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = nextDayOff,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(170.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = schoolDaysLeft,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.weight(0.8f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    LinearProgressIndicator(
                        progress = { schoolYearProgress },
                        trackColor = Color.Transparent,
                        color = MaterialTheme.colorScheme.primary,
                        gapSize = 0.dp,
                        drawStopIndicator = {},
                        strokeCap = StrokeCap.Butt,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    Text(
                        text = "${(schoolYearProgress * 100).roundToInt()}%",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NextBreakTheme {
        MainScreen(MainViewModel(Application()))
    }
}