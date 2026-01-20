package com.julianjesacher.nextbreak.ui.widgets

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
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
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
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
        var daysUntilHolidays = "-"
        var schoolDaysLeft = 0
        var isCalendarToOld = false
        var isSummerHoliday = false

        if(calendar != null) {
            isCalendarToOld = CalendarCalculator.isCalendarToOld()
            isSummerHoliday = CalendarCalculator.isSummerHoliday(calendar)

            if(!isSummerHoliday) {
                val holidayIndex = CalendarCalculator.nextHolidayIndex(calendar)

                if(!CalendarCalculator.isHoliday(calendar)) {
                    daysUntilHolidays = CalendarCalculator.daysUntilHolidays(calendar, holidayIndex).toString()
                }

                schoolDaysLeft = CalendarCalculator.schoolDaysLeft(calendar)
            }
        }

        provideContent {
            GlanceTheme(colors = NextBreakWidgetTheme.colors) {
                Log.d(AppConstants.LOG_TAG, LocalSize.current.toString())
                val size = LocalSize.current
                val isTall = size.height >= 140.dp

                var detailLevel = DetailLevel.MINIMAL
                var fontSize = NextBreakWidgetTheme.widgetTextStyle.fontSize


                var statusTextFontSize = 35
                var statusTextDetailLevel = DetailLevel.MINIMAL

                if(!isTall) {
                    if(size.width >= 200.dp && size.width < 300.dp) {
                        fontSize = 35.sp
                    }
                    else if(size.width >= 300.dp) {
                        detailLevel = DetailLevel.SHORT
                        statusTextDetailLevel = DetailLevel.SHORT
                    }
                }
                else {
                    if(size.width < 200.dp) {
                        fontSize = 45.sp
                    }
                    else if(size.width < 300.dp) {
                        fontSize = 38.sp
                        detailLevel = DetailLevel.SHORT
                    }
                    else {
                        fontSize = 38.sp
                        detailLevel = DetailLevel.FULL
                        statusTextDetailLevel = DetailLevel.FULL
                    }
                }

                Log.d(AppConstants.LOG_TAG, statusTextDetailLevel.toString())

                val holidayText = @Composable {
                    WidgetText(
                        days = daysUntilHolidays,
                        shortLabel = "Break: ",
                        fullLabel = "Next break: ",
                        level = detailLevel,
                        style = NextBreakWidgetTheme.widgetTextStyle.copy(color = NextBreakWidgetTheme.colors.secondary, fontSize = fontSize)
                    )
                }
                val totalText = @Composable {
                    WidgetText(days = schoolDaysLeft.toString(),
                        shortLabel = "Total: ",
                        fullLabel = "Total left: ",
                        level = detailLevel,
                        style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = fontSize)
                    )
                }

                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ImageProvider(R.drawable.widget_surface))
                        .clickable(actionStartActivity<MainActivity>())
                        .padding(vertical = 16.dp, horizontal = 5.dp)
                ) {

                    if(calendar == null || isCalendarToOld || true) {
                        WidgetStatusText(
                            shortLabel = "No data",
                            fullLabel = "No data available",
                            description = "Unable to load data.",
                            level = statusTextDetailLevel,
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = statusTextFontSize.sp)
                        )
                    }
                    else if(isSummerHoliday) {
                        WidgetStatusText(
                            shortLabel = "It's summer!",
                            fullLabel = "",
                            description = "Relax, bro.",
                            level = statusTextDetailLevel,
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = statusTextFontSize.sp)
                        )
                    }
                    else {
                        if(isTall) {
                            Column(
                                modifier = GlanceModifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = GlanceModifier.defaultWeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    holidayText()
                                }
                                Box(
                                    modifier = GlanceModifier.defaultWeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    totalText()
                                }
                            }
                        }
                        else {
                            Row(
                                modifier = GlanceModifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Box(
                                    modifier = GlanceModifier.defaultWeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    holidayText()
                                }
                                Box(
                                    modifier = GlanceModifier.defaultWeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    totalText()
                                }
                            }
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