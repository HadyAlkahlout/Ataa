package com.raiyansoft.eata.model.cases


import com.google.gson.annotations.SerializedName

data class Cases(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)