package com.julianjesacher.nextbreak.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            WidgetScheduler.schedule(context)

            val immediateRequest = OneTimeWorkRequestBuilder<WidgetWorker>().build()
            WorkManager.getInstance(context).enqueue(immediateRequest)
        }
    }
}