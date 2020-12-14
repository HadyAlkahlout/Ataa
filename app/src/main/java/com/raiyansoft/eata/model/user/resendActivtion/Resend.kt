package com.raiyansoft.eata.model.user.resendActivtion


import com.google.gson.annotations.SerializedName

data class Resend(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)