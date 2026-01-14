package com.julianjesacher.nextbreak.ui.widgets

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
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
import androidx.glance.text.TextAlign
import com.julianjesacher.nextbreak.MainActivity
import com.julianjesacher.nextbreak.R
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.data.CalendarRepository
import com.julianjesacher.nextbreak.data.FileManager
import com.julianjesacher.nextbreak.domain.CalendarCalculator
import com.julianjesacher.nextbreak.ui.theme.NextBreakWidgetTheme

object Widget: GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        FileManager.init(context)

        val calendar = CalendarRepository.loadLocalCalendar()
        var daysUntilHolidays = 0
        var schoolDaysLeft = 0
        var isCalendarToOld = false
        if(calendar != null) {
            isCalendarToOld = CalendarCalculator.isCalendarToOld()

            if(!CalendarCalculator.isOffDay(calendar)) {
                val holidayIndex = CalendarCalculator.nextHolidayIndex(calendar)
                daysUntilHolidays = CalendarCalculator.daysUntilHolidays(calendar, holidayIndex)
                schoolDaysLeft = CalendarCalculator.schoolDaysLeft(calendar)
            }
        }

        provideContent {
            GlanceTheme(colors = NextBreakWidgetTheme.colors) {
                Log.d(AppConstants.LOG_TAG, LocalSize.current.toString())
                Row(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ImageProvider(R.drawable.widget_surface))
                        .clickable(actionStartActivity<MainActivity>())
                        .padding(vertical = 16.dp, horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    if(calendar == null || isCalendarToOld) {
                        Text(
                            text = "No data",
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = 30.sp, textAlign = TextAlign.Center)
                        )
                    }
                    else if(CalendarCalculator.isOffDay(calendar)) {
                        Text(
                            text = "Day off!",
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = 30.sp, textAlign = TextAlign.Center)
                        )
                    } else {
                        Box(
                            modifier = GlanceModifier.defaultWeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = daysUntilHolidays.toString(),
                                style = NextBreakWidgetTheme.widgetTextStyle.copy(color = NextBreakWidgetTheme.colors.secondary)
                            )
                        }
                        Box(
                            modifier = GlanceModifier.defaultWeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = schoolDaysLeft.toString(),
                                style = NextBreakWidgetTheme.widgetTextStyle.copy()
                            )
                        }
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