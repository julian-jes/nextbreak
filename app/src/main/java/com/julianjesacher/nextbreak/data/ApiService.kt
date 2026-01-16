package com.julianjesacher.nextbreak.data

import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.models.AppRelease
import com.julianjesacher.nextbreak.models.Calendar
import com.julianjesacher.nextbreak.models.Version
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(AppConstants.VERSION_URL)
    suspend fun getVersion(): Response<Version>

    @GET(AppConstants.CALENDAR_URL)
    suspend fun getData(): Response<Calendar>

    @GET(AppConstants.APP_RELEASE_URL)
    suspend fun getAppRelease(): Response<AppRelease>
}