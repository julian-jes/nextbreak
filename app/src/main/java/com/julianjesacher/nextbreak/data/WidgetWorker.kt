package com.julianjesacher.nextbreak.data

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.julianjesacher.nextbreak.ui.widgets.Widget

class WidgetWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        FileManager.init(applicationContext)

        val versionResult = VersionRepository.checkVersion(applicationContext)
        if(versionResult is CheckVersionResult.Success && versionResult.updateAvailable) {
            val calendarResult = CalendarRepository.downloadCalendar(applicationContext)
            if(calendarResult is DownloadCalendarResult.Success) {
                VersionRepository.saveVersionLocally()
            }
        }

        Widget.updateAll(applicationContext)
        return Result.success()
    }
}