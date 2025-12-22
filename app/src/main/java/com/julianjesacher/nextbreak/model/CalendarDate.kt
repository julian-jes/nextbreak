package com.julianjesacher.nextbreak.model

import com.google.gson.annotations.SerializedName

data class CalendarDate(
    @SerializedName("date")
    val date: String,

    @SerializedName("is_school_day")
    val isSchoolDay: Boolean
)