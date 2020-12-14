package com.raiyansoft.eata.model.setting


import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)