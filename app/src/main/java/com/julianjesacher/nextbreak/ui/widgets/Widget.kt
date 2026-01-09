package com.julianjesacher.nextbreak.ui.widgets

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.julianjesacher.nextbreak.MainActivity
import com.julianjesacher.nextbreak.R
import com.julianjesacher.nextbreak.data.CalendarRepository
import com.julianjesacher.nextbreak.data.FileManager
import com.julianjesacher.nextbreak.domain.CalendarCalculator
import com.julianjesacher.nextbreak.ui.theme.NextBreakWidgetTheme

object Widget: GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Single

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        FileManager.init(context)

        val calendar = CalendarRepository.loadLocalCalendar()
        var daysUntilHolidays = "-"
        var schoolDaysLeft = "-"

        if(calendar != null) {
            daysUntilHolidays = CalendarCalculator.daysUntilHolidays(calendar).toString()
            schoolDaysLeft = CalendarCalculator.schoolDaysLeft(calendar).toString()
        }

        provideContent {
            GlanceTheme(colors = NextBreakWidgetTheme.colors) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ImageProvider(R.drawable.widget_surface))
                        .clickable(actionStartActivity<MainActivity>())
                        .padding(vertical = 16.dp, horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = GlanceModifier.defaultWeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = daysUntilHolidays,
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(color = NextBreakWidgetTheme.colors.secondary)
                        )
                    }
                    Box(
                        modifier = GlanceModifier.defaultWeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = schoolDaysLeft,
                            style = NextBreakWidgetTheme.widgetTextStyle.copy()
                        )
                    }
                }
            }
        }
    }
}

class WidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = Widget
}