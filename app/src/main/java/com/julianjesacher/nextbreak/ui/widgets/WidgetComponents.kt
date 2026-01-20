package com.julianjesacher.nextbreak.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.julianjesacher.nextbreak.ui.theme.NextBreakWidgetTheme

enum class DetailLevel {
    MINIMAL,
    SHORT,
    FULL
}

@Composable
fun WidgetText(
    days: String,
    shortLabel: String,
    fullLabel: String,
    level: DetailLevel,
    style: TextStyle = NextBreakWidgetTheme.widgetTextStyle.copy(),
    modifier: GlanceModifier = GlanceModifier
) {
    val displayText = when(level) {
        DetailLevel.MINIMAL -> days
        DetailLevel.SHORT -> shortLabel + days
        DetailLevel.FULL -> fullLabel + days
    }

    Text(
        text = displayText,
        style = style,
        modifier = modifier
    )
}

@Composable
fun WidgetStatusText(
    shortLabel: String,
    fullLabel: String,
    description: String,
    level: DetailLevel,
    style: TextStyle = NextBreakWidgetTheme.widgetTextStyle.copy(),
    modifier: GlanceModifier = GlanceModifier
) {

    var label = shortLabel
    val baseSize = style.fontSize?.value ?: 16f
    val test = (baseSize - 5).sp
    if(level != DetailLevel.MINIMAL) {
        label = fullLabel
    }

    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = style.copy(color = NextBreakWidgetTheme.colors.primary),
            modifier = modifier
        )

        if(level == DetailLevel.FULL) {
            Spacer(GlanceModifier.height(10.dp))
            Text(
                text = description,
                style = style.copy(color = NextBreakWidgetTheme.colors.secondary, fontSize = test),
                modifier = modifier
            )
        }
    }
}