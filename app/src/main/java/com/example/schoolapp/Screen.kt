package com.example.schoolapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.schoolapp.ui.theme.FunnelSans

@Composable
fun MainScreen()
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C061F)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        Text(
            text = "39",
            color = Color(0xFFD89216),
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "This is a text",
            color = Color(0xFFE1D89F),
            fontSize = 30.sp
        )
        Text(
            text = "This is a text",
            color = Color(0xFF374045),
            fontSize = 30.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview()
{
    MainScreen()
}