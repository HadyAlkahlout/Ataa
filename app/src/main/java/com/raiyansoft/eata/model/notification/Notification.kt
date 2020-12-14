package com.raiyansoft.eata.model.notification


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)