package com.julianjesacher.nextbreak.model

import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("year")
    val year: Int,

    @SerializedName("hotfix")
    val hotfix: Int
)
