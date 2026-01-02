package com.julianjesacher.nextbreak.backend

import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.model.Calendar
import com.julianjesacher.nextbreak.model.Version
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(AppConstants.VERSION_URL)
    suspend fun getVersion(): Response<Version>

    @GET(AppConstants.CALENDAR_URL)
    suspend fun getData(): Response<Calendar>
}