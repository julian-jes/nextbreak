package com.julianjesacher.nextbreak.config

object AppConstants {
    const val VERSION_FILE_NAME = "version.json"
    const val CALENDAR_FILE_NAME = "calendar.json"
    const val FEEDBACK_BASE_URL = "https://docs.google.com/forms/d/e/1FAIpQLSf2pZpCMeOYe0k_Hd4zJMYlUVd8l_gfNZqgU-n5feEv-axVew/viewform?usp=pp_url"
    const val FEEDBACK_ENTRY = "&entry.1834084809="
    const val SOURCE_CODE_URL = "https://github.com/julian-jes/nextbreak"
    const val LOG_TAG = "Next Break Logs"

    const val CHECK_UPDATE_INTERVAL_FILE_NAME = "check_update_interval.json"
    const val CHECK_UPDATE_INTERVAL = 4 * 60 * 60 * 1000L

    const val RETROFIT_BASE_URL = "https://julian-jes.github.io"
    const val CALENDAR_URL = "/nextbreak-data/data.json"
    const val VERSION_URL = "/nextbreak-data/version.json"
    const val APP_RELEASE_URL = "https://api.github.com/repos/julian-jes/nextbreak/releases/latest"
}
