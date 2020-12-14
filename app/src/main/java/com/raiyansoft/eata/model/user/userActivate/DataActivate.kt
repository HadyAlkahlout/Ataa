package com.raiyansoft.eata.model.user.userActivate


import com.google.gson.annotations.SerializedName

data class DataActivate(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)