package com.julianjesacher.nextbreak.backend

import com.google.gson.Gson
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.model.Calendar
import okio.IOException
import retrofit2.HttpException

sealed class DownloadCalendarResult {
    data class Success(val calendar: Calendar) : DownloadCalendarResult()
    object NoInternet : DownloadCalendarResult()
    object Error : DownloadCalendarResult()
}

object CalendarRepository {

    suspend fun downloadCalendar(): DownloadCalendarResult {
        val response = try {
            RetrofitInstance.api.getData()
        } catch (e: IOException) {
            return DownloadCalendarResult.NoInternet
        } catch (e: HttpException) {
            return DownloadCalendarResult.Error
        }

        if(response.isSuccessful && response.body() != null) {
            val calendar = response.body()
            if(calendar == null) {
                return DownloadCalendarResult.Error
            }

            val jsonString = Gson().toJson(calendar)
            FileManager.saveFile(AppConstants.CALENDAR_FILE_NAME,jsonString)

            return DownloadCalendarResult.Success(calendar)
        } else {
            return DownloadCalendarResult.Error
        }
    }

    suspend fun getLocalCalendar(): Calendar? {
        val jsonString = FileManager.loadFile(AppConstants.CALENDAR_FILE_NAME)
        if(jsonString == null) {
            return null
        }

        return Gson().fromJson(jsonString, Calendar::class.java)
    }
}