package com.julianjesacher.nextbreak.models

import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("year")
    val year: Int,

    @SerializedName("hotfix")
    val hotfix: Int
)
