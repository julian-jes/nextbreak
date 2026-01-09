package com.julianjesacher.nextbreak.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.julianjesacher.nextbreak.R
import com.julianjesacher.nextbreak.ui.SystemBars

private val AppColorScheme = darkColorScheme(
    background = BackgroundPrimary,
    surface = BackgroundSecondary,

    primary = ContentPrimary,
    secondary = ContentSecondary,
    tertiary = ContentTertiary,
    tertiaryContainer = ContentTertiaryContainer
)

object NextBreakWidgetTheme {
    val colors = ColorProviders(
        light = AppColorScheme,
        dark = AppColorScheme
    )

    val widgetTextStyle = TextStyle(
        color = ColorProvider(ContentPrimary),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        fontFamily = FontFamily("roboto_black")
    )
}

@Composable
fun NextBreakTheme(
    content: @Composable () -> Unit
) {
    SystemBars()

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}