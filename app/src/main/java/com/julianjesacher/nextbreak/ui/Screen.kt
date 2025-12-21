package com.julianjesacher.nextbreak.ui

import android.app.Activity
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme

@Composable
fun MainScreen(viewModel : MainViewModel) {

    val daysUntilHolidays by viewModel.daysUntilHolidaysDisplay.collectAsState()
    val nextDayOff by viewModel.nextDayOffDisplay.collectAsState()
    val schoolDaysLeft by viewModel.schoolDaysLeftDisplay.collectAsState()
    val schoolYearProgress by viewModel.schoolYearProgressDisplay.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Next Break",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 30.dp)
                .statusBarsPadding()
        )
        Box (
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 100.dp, bottom = 170.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
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
                    text = "school days until\nwinter break",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 30.sp,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Next day off in $nextDayOff days",
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
                    text = "$schoolDaysLeft school days left",
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
                        text = "${(schoolYearProgress * 100).toInt()}%",
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

@Composable //TODO Needs review!
fun SystemBars() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        // Make icons white (dark icons = false)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false

        // Use transparent background without deprecated APIs
        window.statusBarColor = Color.Transparent.value.toInt()
        window.navigationBarColor = Color.Transparent.value.toInt()
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NextBreakTheme {
        MainScreen(MainViewModel())
    }
}