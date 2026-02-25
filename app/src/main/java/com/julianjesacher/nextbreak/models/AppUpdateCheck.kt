package com.julianjesacher.nextbreak.models

data class AppUpdateCheck(
    var lastCheck: Long,
    var lastCheckedVersion: List<Int>,
    var lastReleaseUrl: String
)
