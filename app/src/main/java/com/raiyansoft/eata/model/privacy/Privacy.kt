package com.raiyansoft.eata.model.privacy


import com.google.gson.annotations.SerializedName

data class Privacy(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)