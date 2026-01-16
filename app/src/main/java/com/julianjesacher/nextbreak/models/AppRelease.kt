package com.julianjesacher.nextbreak.models

import com.google.gson.annotations.SerializedName

data class AppRelease(
    @SerializedName("tag_name")
    val tagName: String,

    @SerializedName("html_url")
    val url: String
)
