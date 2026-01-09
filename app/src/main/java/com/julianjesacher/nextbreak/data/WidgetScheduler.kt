package com.julianjesacher.nextbreak.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object WidgetScheduler {
    fun schedule(context: Context) {
        val now = LocalDateTime.now()
        val nextMidnight = now.plusDays(1).withHour(0).withMinute(0)
        val delay = Duration.between(now, nextMidnight).toMinutes()

        val request = PeriodicWorkRequestBuilder<WidgetWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}