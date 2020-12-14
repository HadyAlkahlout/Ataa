package com.raiyansoft.eata.model.profile


import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)