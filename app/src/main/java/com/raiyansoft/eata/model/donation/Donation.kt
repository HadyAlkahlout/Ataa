package com.raiyansoft.eata.model.donation


import com.google.gson.annotations.SerializedName

data class Donation(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)