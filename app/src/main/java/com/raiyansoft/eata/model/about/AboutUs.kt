package com.raiyansoft.eata.model.about

import com.google.gson.annotations.SerializedName

data class AboutUs(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)