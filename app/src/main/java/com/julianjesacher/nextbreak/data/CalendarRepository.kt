package com.julianjesacher.nextbreak.data

import android.content.Context
import com.google.gson.Gson
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.models.Calendar
import com.julianjesacher.nextbreak.utils.ConnectivityChecker
import okio.IOException
import retrofit2.HttpException

sealed class DownloadCalendarResult {
    data class Success(val calendar: Calendar) : DownloadCalendarResult()
    object NoInternet : DownloadCalendarResult()
    object Error : DownloadCalendarResult()
}

object CalendarRepository {

    suspend fun downloadCalendar(context: Context): DownloadCalendarResult {
        val response = try {
            RetrofitInstance.api.getData()
        } catch (e: IOException) {
            return if(ConnectivityChecker.hasInternetConnection(context)) {
                DownloadCalendarResult.Error
            }
            else {
                DownloadCalendarResult.NoInternet
            }
        } catch (e: HttpException) {
            return DownloadCalendarResult.Error
        }

        if(response.isSuccessful && response.body() != null) {
            val calendar = response.body() ?: return DownloadCalendarResult.Error

            val jsonString = Gson().toJson(calendar)
            FileManager.saveFile(AppConstants.CALENDAR_FILE_NAME,jsonString)

            return DownloadCalendarResult.Success(calendar)
        } else {
            return DownloadCalendarResult.Error
        }
    }

    suspend fun loadLocalCalendar(): Calendar? {
        val jsonString = FileManager.loadFile(AppConstants.CALENDAR_FILE_NAME) ?: return null

        return Gson().fromJson(jsonString, Calendar::class.java)
    }
}