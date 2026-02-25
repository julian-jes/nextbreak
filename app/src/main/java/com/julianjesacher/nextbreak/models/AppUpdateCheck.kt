package com.julianjesacher.nextbreak.models

import com.google.gson.annotations.SerializedName

data class AppUpdateCheck(
    @SerializedName("last_check")
    var lastCheck: Long,

    @SerializedName("last_checked_version")
    var lastCheckedVersion: List<Int>,

    @SerializedName("last_release_url")
    var lastReleaseUrl: String
)
