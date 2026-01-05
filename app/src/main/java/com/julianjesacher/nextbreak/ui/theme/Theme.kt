package com.julianjesacher.nextbreak.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.julianjesacher.nextbreak.ui.SystemBars

private val AppColorScheme = darkColorScheme(
    background = BackgroundPrimary,
    surface = BackgroundSecondary,

    primary = ContentPrimary,
    secondary = ContentSecondary,
    tertiary = ContentTertiary,
    tertiaryContainer = ContentTertiaryContainer
)

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