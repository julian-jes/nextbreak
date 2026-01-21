package com.julianjesacher.nextbreak.ui.widgets

import android.content.Context
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
                val size = LocalSize.current
                val isTall = size.height >= 140.dp

                var widgetTextDetailLevel = DetailLevel.MINIMAL
                var widgetTextFontSize = NextBreakWidgetTheme.widgetTextStyle.fontSize


                var noDataTextDetailLevel = DetailLevel.MINIMAL
                var noDataTextFontSize = NextBreakWidgetTheme.widgetTextStyle.fontSize

                var summerTextDetailLevel = DetailLevel.MINIMAL
                var summerTextFontSize = 30.sp

                if(!isTall) {
                    if(size.width >= 200.dp && size.width < 300.dp) {
                        widgetTextFontSize = 35.sp
                        noDataTextFontSize = 35.sp
                        summerTextDetailLevel = DetailLevel.SHORT
                    }
                    else if(size.width >= 300.dp) {
                        widgetTextDetailLevel = DetailLevel.SHORT
                        noDataTextDetailLevel = DetailLevel.SHORT
                        summerTextDetailLevel = DetailLevel.SHORT
                        summerTextFontSize = 35.sp
                    }
                }
                else {
                    if(size.width < 200.dp) {
                        widgetTextFontSize = 45.sp
                        noDataTextFontSize = 43.sp
                        summerTextDetailLevel = DetailLevel.SHORT
                        summerTextFontSize = 32.sp
                    }
                    else if(size.width < 300.dp) {
                        widgetTextFontSize = 36.sp
                        widgetTextDetailLevel = DetailLevel.SHORT
                        noDataTextFontSize = 43.sp
                        summerTextDetailLevel = DetailLevel.FULL
                        summerTextFontSize = 35.sp
                    }
                    else {
                        widgetTextFontSize = 36.sp
                        widgetTextDetailLevel = DetailLevel.FULL
                        noDataTextDetailLevel = DetailLevel.FULL
                        noDataTextFontSize = 35.sp
                        summerTextDetailLevel = DetailLevel.FULL
                        summerTextFontSize = 38.sp
                    }
                }

                val holidayText = @Composable {
                    WidgetText(
                        days = daysUntilHolidays,
                        shortLabel = "Break: ",
                        fullLabel = "Next break: ",
                        level = widgetTextDetailLevel,
                        style = NextBreakWidgetTheme.widgetTextStyle.copy(color = NextBreakWidgetTheme.colors.secondary, fontSize = widgetTextFontSize)
                    )
                }
                val totalText = @Composable {
                    WidgetText(days = schoolDaysLeft.toString(),
                        shortLabel = "Total: ",
                        fullLabel = "Total left: ",
                        level = widgetTextDetailLevel,
                        style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = widgetTextFontSize)
                    )
                }

                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ImageProvider(R.drawable.widget_surface))
                        .clickable(actionStartActivity<MainActivity>())
                        .padding(vertical = 16.dp, horizontal = 5.dp)
                ) {

                    if(calendar == null || isCalendarToOld) {
                        WidgetStatusText(
                            shortLabel = "No data",
                            fullLabel = "No data available",
                            description = "Unable to load data.",
                            level = noDataTextDetailLevel,
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = noDataTextFontSize)
                        )
                    }
                    else if(isSummerHoliday) {
                        WidgetStatusText(
                            shortLabel = "Summer!",
                            fullLabel = "It's summer!",
                            description = "Relax, bro.",
                            level = summerTextDetailLevel,
                            style = NextBreakWidgetTheme.widgetTextStyle.copy(fontSize = summerTextFontSize)
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