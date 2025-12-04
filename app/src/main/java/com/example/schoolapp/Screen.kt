package com.example.schoolapp

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import com.example.schoolapp.ui.theme.FunnelSans

@Composable
fun MainScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Title",
            color = Color(0xFFf0c184),
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "39",
                color = Color(0xFFd89216),
                fontSize = 100.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "school days until\nwinter break",
                color = Color(0xFFf0c184),
                fontSize = 30.sp,
                lineHeight = 35.sp,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(Color(0xFF212121))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF404040))

            ) {
                LinearProgressIndicator(
                    progress = { 0.8f },
                    trackColor = Color.Transparent,
                    color = Color(0xFFd89216),
                    gapSize = 0.dp,
                    drawStopIndicator = {},
                    strokeCap = StrokeCap.Butt,
                    modifier = Modifier
                        .fillMaxSize()
                )
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
    MainScreen()
}