package com.julianjesacher.nextbreak.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.gson.Gson
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.data.DownloadCalendarResult
import com.julianjesacher.nextbreak.data.FileManager
import com.julianjesacher.nextbreak.data.RetrofitInstance
import com.julianjesacher.nextbreak.models.AppRelease
import com.julianjesacher.nextbreak.models.Calendar
import okio.IOException
import retrofit2.HttpException

sealed class CheckUpdatesResult {
    data class Success(val updateAvailable: Boolean, val releaseUrl: String) : CheckUpdatesResult()
    object NoInternet : CheckUpdatesResult()
    object Error : CheckUpdatesResult()
}

object AppVersionUtils {
    fun getLocalVersion(context: Context): String {
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

    suspend fun checkForUpdates(context: Context): CheckUpdatesResult {
        val response = try {
            RetrofitInstance.api.getAppRelease()
        } catch (e: IOException) {
            return if(ConnectivityChecker.hasInternetConnection(context)) {
                CheckUpdatesResult.Error
            }
            else {
                CheckUpdatesResult.NoInternet
            }
        } catch (e: HttpException) {
            return CheckUpdatesResult.Error
        }

        if(response.isSuccessful && response.body() != null) {
            val appRelease = response.body() ?: return CheckUpdatesResult.Error

            val localVersionString = getLocalVersion(context).replace("-debug", "")
            val localVersion = localVersionString.split(".").map { it.toInt() }

            val onlineVersionString = appRelease.tagName.replace("v", "")
            val onlineVersion = onlineVersionString.split(".").map { it.toInt() }

            return CheckUpdatesResult.Success(isUpdate(localVersion, onlineVersion), appRelease.url)
        } else {
            return CheckUpdatesResult.Error
        }
    }

    private fun isUpdate(localVersion: List<Int>, onlineVersion: List<Int>): Boolean {
        for (i in 0..< 3) {

            for(j in 0..< i) {
                if(onlineVersion[i] < localVersion[i]) {
                    return false
                }
            }

            if(onlineVersion[i] > localVersion[i]) {
                return true
            }
        }

        return false
    }
}