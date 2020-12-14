package com.raiyansoft.eata.model.conditions


import com.google.gson.annotations.SerializedName

data class Conditions(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)