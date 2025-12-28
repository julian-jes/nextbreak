package com.julianjesacher.nextbreak.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme

@Composable
fun DayOffScreen() {
    BaseScreen(
        onInfoClick = {}
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
                text = "No school\ntoday!",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 55.sp,
                lineHeight = 60.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Relax, bro.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 30.sp,
                lineHeight = 35.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayOffScreenPreview() {
    NextBreakTheme {
        DayOffScreen()
    }
}