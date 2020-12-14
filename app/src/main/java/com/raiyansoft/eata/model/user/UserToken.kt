package com.raiyansoft.eata.model.user


import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)