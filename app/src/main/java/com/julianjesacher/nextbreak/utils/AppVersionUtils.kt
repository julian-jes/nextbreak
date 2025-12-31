package com.julianjesacher.nextbreak.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object AppVersionUtils {
    fun getAppVersion(context: Context): String {
        return try {
            val versionName : String? = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0)).versionName
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            }
            versionName ?: ""
        } catch(ex: Exception) {
            ""
        }
    }
}