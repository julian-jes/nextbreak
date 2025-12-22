package com.julianjesacher.nextbreak.model

import com.google.gson.annotations.SerializedName

data class Calendar(
    @SerializedName("autumn_break_start")
    val autumnBreakStart : String,

    @SerializedName("winter_break_start")
    val winterBreakStart : String,

    @SerializedName("carnival_break_start")
    val carnivalBreakStart : String,

    @SerializedName("easter_break_start")
    val easterBreakStart : String,

    @SerializedName("calendar")
    val calendar: List<CalendarDate>
)
