package com.julianjesacher.nextbreak.backend

import com.julianjesacher.nextbreak.model.Calendar
import com.julianjesacher.nextbreak.model.Version
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/nextbreak-data/version.json")
    suspend fun getVersion(): Response<Version>

    @GET("/nextbreak-data/data.json")
    suspend fun getData(): Response<Calendar>
}